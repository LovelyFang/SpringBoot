package com.tsxy.scheduledtasks.job.quartz;

import java.lang.reflect.InvocationTargetException;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.JobMethodInvocationFailedException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.MethodInvoker;

/**
 * @Author Liu_df
 * @Date 2022/11/22 0:35
 */
public class MethodInvokingJobDetailFactoryBean extends ArgumentConvertingMethodInvoker implements FactoryBean<JobDetail>, InitializingBean, ApplicationContextAware , BeanNameAware, BeanClassLoaderAware, BeanFactoryAware {

    private String name;

    private String group = Scheduler.DEFAULT_GROUP;

    private boolean concurrent = true;

    private String targetBeanName;

    private String beanName;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private BeanFactory beanFactory;

    private JobDetail jobDetail;

    private ApplicationContext applicationContext;

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
        return ClassUtils.forName(className, this.beanClassLoader);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException {
        prepare();

        String name = (this.name != null ? this.name : this.beanName);

        Class<?> jobClass = (this.concurrent ? MethodInvokingJob.class : StatefulMethodInvokingJob.class);

        JobDetailImpl jdi = new JobDetailImpl();
        jdi.setName(name);
        jdi.setGroup(this.group);
        jdi.setJobClass((Class) jobClass);
        jdi.setDurability(true);

        JobDataMap jobDataMap = (JobDataMap) jdi.getJobDataMap();
        jobDataMap.put("methodInvoker", this);
        // jobDataMap.put("jdbcTemplate", jdbcTemplate);
        // jobDataMap.put("tableName", tableName);
        jobDataMap.put("concurrent", concurrent);

        this.jobDetail = jdi;

        postProcessJobDetail(this.jobDetail);
    }

    protected void postProcessJobDetail(JobDetail jobDetail) {
    }

    @Override
    public Class<?> getTargetClass() {
        Class<?> targetClass = super.getTargetClass();
        if (targetClass == null && this.targetBeanName != null) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set when using 'targetBeanName'");
            targetClass = this.beanFactory.getType(this.targetBeanName);
        }
        return targetClass;
    }

    @Override
    public Object getTargetObject() {
        Object targetObject = super.getTargetObject();
        if (targetObject == null && this.targetBeanName != null) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set when using 'targetBeanName'");
            targetObject = this.beanFactory.getBean(this.targetBeanName);
        }
        return targetObject;
    }

    @Override
    public JobDetail getObject() {
        return this.jobDetail;
    }

    @Override
    public Class<? extends JobDetail> getObjectType() {
        return (this.jobDetail != null ? this.jobDetail.getClass() : JobDetail.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public static class MethodInvokingJob extends QuartzJobBean {

        protected static final Logger logger = LoggerFactory.getLogger(MethodInvokingJob.class);
        private MethodInvoker methodInvoker;

        // private JdbcTemplate jdbcTemplate;
        //
        // private String tableName;

        private boolean concurrent;

        public void setMethodInvoker(MethodInvoker methodInvoker) {
            this.methodInvoker = methodInvoker;
        }


        public void setConcurrent(boolean concurrent) {
            this.concurrent = concurrent;
        }

        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            JobKey key = context.getJobDetail().getKey();
            try {
                if (AppConfig.getInt("skip.all", 0) > 0) {
                    logger.info("skip.all > 0, 跳过定时任务:" + key.getGroup().toLowerCase() + "-" + key.getName());
                    return;
                }
                if (AppConfig.getInt("run." + key.getGroup().toLowerCase() + "." + key.getName(), 1) <= 0) {
                    logger.info("run." + key.getGroup().toLowerCase() + "." + key.getName() + " <= 0,跳过定时任务:"
                            + key.getGroup().toLowerCase() + "-" + key.getName());
                    return;
                }
                context.setResult(this.methodInvoker.invoke());
            } catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof JobExecutionException) {
                    throw (JobExecutionException) ex.getTargetException();
                } else {
                    throw new JobMethodInvocationFailedException(this.methodInvoker, ex.getTargetException());
                }
            } catch (Exception ex) {
                throw new JobMethodInvocationFailedException(this.methodInvoker, ex);
            } finally {
                // persistence.updateJob(context, tableName, id, status);
            }
        }
    }

    @PersistJobDataAfterExecution
    @DisallowConcurrentExecution
    public static class StatefulMethodInvokingJob extends MethodInvokingJob {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}