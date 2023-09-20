package com.common.utils.ssh_pool;

import org.apache.commons.pool2.impl.*;
import org.apache.sshd.client.session.ClientSession;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.time.Duration;

/**
 * description: cn.com.mina.pool
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/7/14
 * version: 1.0
 */
public class SSHPool {

    private GenericKeyedObjectPool<SSHConfig, ClientSession> pool;

    public SSHPool() {
        this.pool = AccessController.doPrivileged((PrivilegedAction<GenericKeyedObjectPool<SSHConfig, ClientSession>>) SSHPool::getPool);
    }

    public static SSHPool getInstance() {
        return SSHPoolHolder.INSTANCE;
    }

    private static GenericKeyedObjectPool<SSHConfig, ClientSession> getPool() {
        GenericKeyedObjectPoolConfig<ClientSession> poolConfig = new GenericKeyedObjectPoolConfig<>();
        // 每种key最小空闲对象数量
        poolConfig.setMinIdlePerKey(5);
        // 每种key最大空闲对象数量
        poolConfig.setMaxIdlePerKey(5);
        poolConfig.setMaxTotalPerKey(20);
        // 最大对象数量
        poolConfig.setMaxTotal(100);
        // 最长等待分配时间
        poolConfig.setMaxWait(Duration.ofSeconds(60L));
        // 使用时检查对象（默认不检查）
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestOnReturn(true);

        // 驱逐线程每次检查对象个数
        poolConfig.setNumTestsPerEvictionRun(2);
        // 空闲连接被驱逐前能够保留的时间
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(60L));
        // 当空闲线程大于minIdle，空闲连接能够保留时间，同时指定会被上面的覆盖
        poolConfig.setSoftMinEvictableIdleTime(Duration.ofSeconds(120L));
        // 驱逐线程执行间隔时间
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(60L));

        //放弃长时间占用连接的对象
        AbandonedConfig abandonedConfig = new AbandonedConfig();
        //如果处理发了遗弃对象的回收和清理，是否要打印该对象的调用堆栈。默认值为false
        abandonedConfig.setLogAbandoned(false);
        // 默认为false
        abandonedConfig.setUseUsageTracking(false);
        //如果设置为true，就意味着，在borrowObject方法中（从对象池中申请对象的时候）就可以进行遗弃对象的相关清理逻辑。
        //很长时间是多长的，取决于removeAbandonedTimeout配置了多少，默认300秒。
        abandonedConfig.setRemoveAbandonedOnBorrow(true);
        //对象池本省可以通过GenericObjectPoolConfig配置后台异步清理任务，但是后台清理任务的主要职责是关注空闲（状态为空闲）对象的检测和清理。
        //如果removeAbandonedOnMaintenance设置为true，就意味着，在对象池的异步清理任务中，也可以进行遗弃（状态为活跃）对象的相关清理。
        abandonedConfig.setRemoveAbandonedOnMaintenance(true);
        //这个时间，默认300s。如果对象池的一个对象被占用了超过300s还没有被释放，就认为是被调用方遗弃了。
        abandonedConfig.setRemoveAbandonedTimeout(Duration.ofMinutes(30L));

        SSHPooledObjectFactory factory = new SSHPooledObjectFactory();
        return new GenericKeyedObjectPool<>(factory, poolConfig, abandonedConfig);
    }

    public ClientSession borrowObject(SSHConfig config) throws Exception {
        return this.pool.borrowObject(config);
    }

    public void returnObject(SSHConfig config, ClientSession session) {
        this.pool.returnObject(config, session);
    }

    public void clearAll(SSHConfig config) {
        this.pool.clear(config);
    }


    private static class SSHPoolHolder {
        private static SSHPool INSTANCE = new SSHPool();
    }
}
