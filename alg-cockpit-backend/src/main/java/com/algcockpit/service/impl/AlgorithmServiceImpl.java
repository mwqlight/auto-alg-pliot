package com.algcockpit.service.impl;

import com.algcockpit.dto.request.AlgorithmExecuteRequest;
import com.algcockpit.dto.response.AlgorithmExecutionResponse;
import com.algcockpit.entity.Algorithm;
import com.algcockpit.entity.AlgorithmExecution;
import com.algcockpit.exception.BusinessException;
import com.algcockpit.repository.AlgorithmExecutionRepository;
import com.algcockpit.repository.AlgorithmRepository;
import com.algcockpit.service.AlgorithmService;
import com.algcockpit.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 算法信息服务实现类
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Service
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmRepository, Algorithm> implements AlgorithmService {

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private AlgorithmExecutionRepository algorithmExecutionRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Algorithm> getAlgorithmsByCategoryId(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throw new BusinessException(400, "分类ID不能为空");
        }
        
        try {
            List<Algorithm> algorithms = algorithmRepository.selectByCategoryId(categoryId);
            log.info("根据分类ID查询算法列表成功，分类ID: {}, 数量: {}", categoryId, algorithms.size());
            return algorithms;
        } catch (Exception e) {
            log.error("根据分类ID查询算法列表失败，分类ID: {}", categoryId, e);
            throw new BusinessException(500, "查询算法列表失败");
        }
    }

    @Override
    public IPage<Algorithm> getAlgorithmPage(Long pageNum, Long pageSize, Long categoryId, 
                                           Integer difficultyLevel, Integer status, String keyword) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1L;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10L;
        }
        
        try {
            Page<Algorithm> page = new Page<>(pageNum, pageSize);
            IPage<Algorithm> result = algorithmRepository.selectAlgorithmPage(page, categoryId, 
                    difficultyLevel, status, keyword);
            log.info("分页查询算法列表成功，页码: {}, 页大小: {}, 总数: {}", pageNum, pageSize, result.getTotal());
            return result;
        } catch (Exception e) {
            log.error("分页查询算法列表失败，页码: {}, 页大小: {}", pageNum, pageSize, e);
            throw new BusinessException(500, "分页查询算法列表失败");
        }
    }

    @Override
    public Algorithm getAlgorithmDetail(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        try {
            Algorithm algorithm = algorithmRepository.selectById(id);
            if (algorithm == null || algorithm.getDeleted() == 1) {
                throw new BusinessException(404, "算法不存在");
            }
            
            // 增加热度
            increasePopularity(id);
            
            log.info("获取算法详情成功，算法ID: {}, 名称: {}", id, algorithm.getName());
            return algorithm;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取算法详情失败，算法ID: {}", id, e);
            throw new BusinessException(500, "获取算法详情失败");
        }
    }

    @Override
    @Transactional
    public Algorithm createAlgorithm(Algorithm algorithm) {
        if (algorithm == null) {
            throw new BusinessException(400, "算法信息不能为空");
        }
        if (algorithm.getName() == null || algorithm.getName().trim().isEmpty()) {
            throw new BusinessException(400, "算法名称不能为空");
        }
        if (algorithm.getCategoryId() == null || algorithm.getCategoryId() <= 0) {
            throw new BusinessException(400, "分类ID不能为空");
        }
        
        // 检查算法名称是否已存在
        Algorithm existingAlgorithm = algorithmRepository.selectByName(algorithm.getName());
        if (existingAlgorithm != null) {
            throw new BusinessException(400, "算法名称已存在");
        }
        
        try {
            // 设置默认值
            algorithm.setId(null);
            algorithm.setPopularity(0);
            algorithm.setStatus(1);
            algorithm.setDeleted(0);
            algorithm.setCreatedTime(LocalDateTime.now());
            algorithm.setUpdatedTime(LocalDateTime.now());
            
            int result = algorithmRepository.insert(algorithm);
            if (result <= 0) {
                throw new BusinessException(500, "创建算法失败");
            }
            
            log.info("创建算法成功，算法ID: {}, 名称: {}", algorithm.getId(), algorithm.getName());
            return algorithm;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建算法失败，算法名称: {}", algorithm.getName(), e);
            throw new BusinessException(500, "创建算法失败");
        }
    }

    @Override
    @Transactional
    public Algorithm updateAlgorithm(Algorithm algorithm) {
        if (algorithm == null || algorithm.getId() == null || algorithm.getId() <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        if (algorithm.getName() == null || algorithm.getName().trim().isEmpty()) {
            throw new BusinessException(400, "算法名称不能为空");
        }
        
        // 检查算法是否存在
        Algorithm existingAlgorithm = algorithmRepository.selectById(algorithm.getId());
        if (existingAlgorithm == null || existingAlgorithm.getDeleted() == 1) {
            throw new BusinessException(404, "算法不存在");
        }
        
        // 检查算法名称是否与其他算法冲突
        if (!Objects.equals(existingAlgorithm.getName(), algorithm.getName())) {
            Algorithm conflictAlgorithm = algorithmRepository.selectByName(algorithm.getName());
            if (conflictAlgorithm != null) {
                throw new BusinessException(400, "算法名称已存在");
            }
        }
        
        try {
            algorithm.setUpdatedTime(LocalDateTime.now());
            int result = algorithmRepository.updateById(algorithm);
            if (result <= 0) {
                throw new BusinessException(500, "更新算法失败");
            }
            
            log.info("更新算法成功，算法ID: {}, 名称: {}", algorithm.getId(), algorithm.getName());
            return algorithm;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新算法失败，算法ID: {}", algorithm.getId(), e);
            throw new BusinessException(500, "更新算法失败");
        }
    }

    @Override
    @Transactional
    public boolean deleteAlgorithm(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        // 检查算法是否存在
        Algorithm algorithm = algorithmRepository.selectById(id);
        if (algorithm == null || algorithm.getDeleted() == 1) {
            throw new BusinessException(404, "算法不存在");
        }
        
        try {
            algorithm.setDeleted(1);
            algorithm.setUpdatedTime(LocalDateTime.now());
            int result = algorithmRepository.updateById(algorithm);
            
            if (result > 0) {
                log.info("删除算法成功，算法ID: {}", id);
                return true;
            } else {
                log.error("删除算法失败，算法ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("删除算法失败，算法ID: {}", id, e);
            throw new BusinessException(500, "删除算法失败");
        }
    }

    @Override
    @Transactional
    public void increasePopularity(Long id) {
        if (id == null || id <= 0) {
            return;
        }
        
        try {
            algorithmRepository.increasePopularity(id);
            log.debug("增加算法热度成功，算法ID: {}", id);
        } catch (Exception e) {
            log.error("增加算法热度失败，算法ID: {}", id, e);
        }
    }

    @Override
    public AlgorithmExecutionResponse executeAlgorithm(AlgorithmExecuteRequest request) {
        // 实现算法执行逻辑
        AlgorithmExecutionResponse response = new AlgorithmExecutionResponse();
        response.setStatus(0); // 执行中
        response.setExecutionTime(0L);
        response.setMemoryUsage(0L);
        
        // 这里需要实现具体的算法执行逻辑
        // 包括：代码编译、沙箱执行、性能监控等
        
        log.info("算法执行请求，算法ID: {}", request.getAlgorithmId());
        return response;
    }

    @Override
    public Page<AlgorithmExecution> getExecutionRecords(Long userId, Page<AlgorithmExecution> page) {
        try {
            QueryWrapper<AlgorithmExecution> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .orderByDesc("create_time");
            
            return algorithmExecutionRepository.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取执行记录失败，用户ID: {}", userId, e);
            throw new BusinessException(500, "获取执行记录失败");
        }
    }

    @Override
    public AlgorithmExecution getExecutionDetail(Long executionId) {
        if (executionId == null || executionId <= 0) {
            throw new BusinessException(400, "执行记录ID不能为空");
        }
        
        try {
            AlgorithmExecution execution = algorithmExecutionRepository.selectById(executionId);
            if (execution == null) {
                throw new BusinessException(404, "执行记录不存在");
            }
            
            return execution;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取执行详情失败，执行记录ID: {}", executionId, e);
            throw new BusinessException(500, "获取执行详情失败");
        }
    }

    @Override
    public boolean stopAlgorithmExecution(Long executionId) {
        if (executionId == null || executionId <= 0) {
            throw new BusinessException(400, "执行记录ID不能为空");
        }
        
        try {
            // 这里需要实现停止算法执行的逻辑
            // 包括：中断执行进程、更新执行状态等
            
            log.info("停止算法执行，执行记录ID: {}", executionId);
            return true;
        } catch (Exception e) {
            log.error("停止算法执行失败，执行记录ID: {}", executionId, e);
            throw new BusinessException(500, "停止算法执行失败");
        }
    }

    @Override
    public Page<Algorithm> getPopularAlgorithms(Page<Algorithm> page) {
        try {
            QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("popularity")
                       .orderByDesc("create_time");
            
            return algorithmRepository.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取热门算法失败", e);
            throw new BusinessException(500, "获取热门算法失败");
        }
    }

    @Override
    public Page<Algorithm> getAlgorithmsByType(String type, Page<Algorithm> page) {
        if (type == null || type.trim().isEmpty()) {
            throw new BusinessException(400, "算法类型不能为空");
        }
        
        try {
            QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", type)
                       .orderByDesc("popularity");
            
            return algorithmRepository.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("根据类型获取算法失败，类型: {}", type, e);
            throw new BusinessException(500, "根据类型获取算法失败");
        }
    }

    @Override
    public Page<Algorithm> searchAlgorithms(String keyword, Page<Algorithm> page) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException(400, "搜索关键词不能为空");
        }
        
        try {
            QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", keyword)
                       .or()
                       .like("description", keyword)
                       .orderByDesc("popularity");
            
            return algorithmRepository.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("搜索算法失败，关键词: {}", keyword, e);
            throw new BusinessException(500, "搜索算法失败");
        }
    }

    @Override
    public Boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        
        try {
            Algorithm algorithm = algorithmRepository.selectById(id);
            return algorithm != null && algorithm.getDeleted() == 0;
        } catch (Exception e) {
            log.error("检查算法是否存在失败，算法ID: {}", id, e);
            return false;
        }
    }
}