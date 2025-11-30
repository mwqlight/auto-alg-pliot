package com.algcockpit.service.impl;

import com.algcockpit.entity.AlgorithmCategory;
import com.algcockpit.exception.BusinessException;
import com.algcockpit.repository.AlgorithmCategoryRepository;
import com.algcockpit.service.AlgorithmCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 算法分类服务实现类
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Service
public class AlgorithmCategoryServiceImpl extends ServiceImpl<AlgorithmCategoryRepository, AlgorithmCategory> 
    implements AlgorithmCategoryService {

    @Override
    public List<AlgorithmCategory> getAllEnabledCategories() {
        return baseMapper.selectAllEnabledCategories();
    }

    @Override
    public AlgorithmCategory getCategoryDetail(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(40001, "分类ID不能为空");
        }
        
        AlgorithmCategory category = baseMapper.selectById(id);
        if (category == null || category.getDeleted() == 1) {
            throw new BusinessException(40002, "算法分类不存在");
        }
        
        return category;
    }

    @Override
    public AlgorithmCategory createCategory(AlgorithmCategory category) {
        // 参数校验
        if (category == null || !StringUtils.hasText(category.getName())) {
            throw new BusinessException(40003, "分类名称不能为空");
        }
        
        // 检查分类名称是否已存在
        LambdaQueryWrapper<AlgorithmCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AlgorithmCategory::getName, category.getName())
                   .eq(AlgorithmCategory::getDeleted, 0);
        
        if (baseMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(40004, "分类名称已存在");
        }
        
        // 设置默认值
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        
        // 保存分类
        boolean success = save(category);
        if (!success) {
            throw new BusinessException(50001, "创建算法分类失败");
        }
        
        log.info("创建算法分类成功: {}", category.getName());
        return category;
    }

    @Override
    public AlgorithmCategory updateCategory(AlgorithmCategory category) {
        if (category == null || category.getId() == null) {
            throw new BusinessException(40005, "分类ID不能为空");
        }
        
        // 检查分类是否存在
        AlgorithmCategory existingCategory = getCategoryDetail(category.getId());
        if (existingCategory == null) {
            throw new BusinessException(40002, "算法分类不存在");
        }
        
        // 如果修改了名称，检查名称是否重复
        if (StringUtils.hasText(category.getName()) && 
            !existingCategory.getName().equals(category.getName())) {
            
            LambdaQueryWrapper<AlgorithmCategory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AlgorithmCategory::getName, category.getName())
                       .ne(AlgorithmCategory::getId, category.getId())
                       .eq(AlgorithmCategory::getDeleted, 0);
            
            if (baseMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException(40004, "分类名称已存在");
            }
        }
        
        // 更新分类
        boolean success = updateById(category);
        if (!success) {
            throw new BusinessException(50002, "更新算法分类失败");
        }
        
        log.info("更新算法分类成功: {}", category.getId());
        return category;
    }

    @Override
    public boolean deleteCategory(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(40001, "分类ID不能为空");
        }
        
        AlgorithmCategory category = getCategoryDetail(id);
        if (category == null) {
            throw new BusinessException(40002, "算法分类不存在");
        }
        
        // 逻辑删除
        category.setDeleted(1);
        boolean success = updateById(category);
        
        if (success) {
            log.info("删除算法分类成功: {}", id);
        } else {
            log.error("删除算法分类失败: {}", id);
        }
        
        return success;
    }
}