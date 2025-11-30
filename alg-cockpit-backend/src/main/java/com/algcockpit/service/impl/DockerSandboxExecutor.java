package com.algcockpit.service.impl;

import com.algcockpit.dto.request.ExecuteRequest;
import com.algcockpit.dto.response.ExecuteResponse;
import com.algcockpit.service.SandboxExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于Docker的安全沙箱执行器
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Service
public class DockerSandboxExecutor implements SandboxExecutor {

    @Value("${sandbox.workspace:/tmp/alg-cockpit}")
    private String workspacePath;

    @Value("${sandbox.docker.image.prefix:alg-cockpit}")
    private String dockerImagePrefix;

    @Value("${sandbox.max.concurrent.executions:10}")
    private int maxConcurrentExecutions;

    private final ExecutorService executorService;
    private final ConcurrentMap<String, ExecuteResponse> asyncResults;

    public DockerSandboxExecutor() {
        this.executorService = Executors.newFixedThreadPool(maxConcurrentExecutions);
        this.asyncResults = new ConcurrentHashMap<>();
    }

    @Override
    public ExecuteResponse execute(ExecuteRequest request) {
        log.info("开始执行算法代码，算法ID: {}, 语言: {}", request.getAlgorithmId(), request.getLanguage());
        
        long startTime = System.currentTimeMillis();
        ExecuteResponse response = new ExecuteResponse();
        
        try {
            // 验证语言支持
            if (!isLanguageSupported(request.getLanguage())) {
                response.setSuccess(false)
                        .setError("不支持的编程语言: " + request.getLanguage())
                        .setStatus("UNSUPPORTED_LANGUAGE");
                return response;
            }

            // 创建执行目录
            String executionId = UUID.randomUUID().toString();
            Path executionDir = createExecutionDirectory(executionId);
            
            // 写入代码文件
            String codeFileName = getCodeFileName(request.getLanguage());
            Path codeFilePath = executionDir.resolve(codeFileName);
            writeCodeFile(codeFilePath, request.getCode());
            
            // 写入输入数据（如果有）
            if (request.getInputData() != null && !request.getInputData().trim().isEmpty()) {
                Path inputFilePath = executionDir.resolve("input.txt");
                writeInputFile(inputFilePath, request.getInputData());
            }

            // 构建Docker命令
            String dockerCommand = buildDockerCommand(request, executionId);
            
            // 执行命令
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "run", "--rm", 
                    "--memory=" + request.getMemoryLimit() + "m",
                    "--cpus=1",
                    "--network=none",
                    "--read-only",
                    "--tmpfs=/tmp:rw,noexec,nosuid,size=64m",
                    "-v", executionDir.toString() + ":/workspace:ro",
                    getDockerImageName(request.getLanguage()));
            
            Process process = processBuilder.start();
            
            // 设置超时
            boolean finished = process.waitFor(request.getTimeout(), java.util.concurrent.TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroy();
                response.setSuccess(false)
                        .setTimeout(true)
                        .setError("执行超时（" + request.getTimeout() + "秒）")
                        .setStatus("TIMEOUT");
            } else {
                // 读取输出
                String output = readProcessOutput(process);
                String error = readProcessError(process);
                
                response.setSuccess(process.exitValue() == 0)
                        .setOutput(output)
                        .setError(error)
                        .setExitCode(process.exitValue())
                        .setTimeout(false)
                        .setMemoryExceeded(false)
                        .setStatus(process.exitValue() == 0 ? "SUCCESS" : "FAILED");
            }
            
            // 读取执行结果文件
            Path outputFilePath = executionDir.resolve("output.txt");
            if (Files.exists(outputFilePath)) {
                String fileOutput = Files.readString(outputFilePath);
                if (response.getOutput() == null || response.getOutput().isEmpty()) {
                    response.setOutput(fileOutput);
                } else {
                    response.setOutput(response.getOutput() + "\n" + fileOutput);
                }
            }
            
        } catch (Exception e) {
            log.error("执行算法代码时发生异常", e);
            response.setSuccess(false)
                    .setError("执行异常: " + e.getMessage())
                    .setStatus("ERROR");
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            response.setExecutionTime(executionTime);
            log.info("算法执行完成，耗时: {}ms, 状态: {}", executionTime, response.getStatus());
        }
        
        return response;
    }

    @Override
    public String executeAsync(ExecuteRequest request) {
        String executionId = UUID.randomUUID().toString();
        
        executorService.submit(() -> {
            ExecuteResponse response = execute(request);
            asyncResults.put(executionId, response);
        });
        
        return executionId;
    }

    @Override
    public ExecuteResponse getAsyncResult(String executionId) {
        return asyncResults.get(executionId);
    }

    @Override
    public Boolean cancelAsyncExecution(String executionId) {
        // 在实际实现中，这里需要更复杂的逻辑来真正取消Docker容器的执行
        // 这里简化实现，只是从结果中移除
        return asyncResults.remove(executionId) != null;
    }

    @Override
    public Boolean isLanguageSupported(String language) {
        return switch (language.toLowerCase()) {
            case "java", "python", "javascript", "cpp", "c" -> true;
            default -> false;
        };
    }

    @Override
    public String getSandboxStatus() {
        return "Docker沙箱运行正常，最大并发执行数: " + maxConcurrentExecutions;
    }

    private Path createExecutionDirectory(String executionId) throws IOException {
        Path executionDir = Paths.get(workspacePath, "executions", executionId);
        Files.createDirectories(executionDir);
        return executionDir;
    }

    private String getCodeFileName(String language) {
        return switch (language.toLowerCase()) {
            case "java" -> "Main.java";
            case "python" -> "main.py";
            case "javascript" -> "main.js";
            case "cpp" -> "main.cpp";
            case "c" -> "main.c";
            default -> "code.txt";
        };
    }

    private void writeCodeFile(Path filePath, String code) throws IOException {
        Files.writeString(filePath, code);
    }

    private void writeInputFile(Path filePath, String inputData) throws IOException {
        Files.writeString(filePath, inputData);
    }

    private String buildDockerCommand(ExecuteRequest request, String executionId) {
        String language = request.getLanguage().toLowerCase();
        String codeFileName = getCodeFileName(language);
        
        return switch (language) {
            case "java" -> String.format("javac /workspace/%s && java -cp /workspace Main", codeFileName);
            case "python" -> String.format("python /workspace/%s", codeFileName);
            case "javascript" -> String.format("node /workspace/%s", codeFileName);
            case "cpp" -> String.format("g++ /workspace/%s -o /workspace/main && /workspace/main", codeFileName);
            case "c" -> String.format("gcc /workspace/%s -o /workspace/main && /workspace/main", codeFileName);
            default -> "echo 'Unsupported language'";
        };
    }

    private String getDockerImageName(String language) {
        return dockerImagePrefix + "-" + language.toLowerCase();
    }

    private String readProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString().trim();
        }
    }

    private String readProcessError(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line).append("\n");
            }
            return error.toString().trim();
        }
    }
}