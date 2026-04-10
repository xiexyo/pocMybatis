package com.bank.poc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器。
 *
 * <p>用于快速确认服务是否已成功启动，可被：
 * <ul>
 *     <li>运维探针</li>
 *     <li>网关健康检查</li>
 *     <li>本地联调</li>
 * </ul>
 * 调用。
 */
@RestController
public class HealthController {

    /**
     * 服务健康检查接口。
     *
     * @return 健康检查结果
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", "B0000");
        result.put("message", "success");
        result.put("service", "retail-bank-aicoding-poc");
        return result;
    }
}