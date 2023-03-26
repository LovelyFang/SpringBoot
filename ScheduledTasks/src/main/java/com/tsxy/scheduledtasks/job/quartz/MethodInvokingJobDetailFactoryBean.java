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

    // private JdbcTemplate jdbcTemplate;

    // private String tableName = "bbg_quartz_job_log";

    private ApplicationContext applicationContext;

    /**
     * Set the name of the job.
     * <p>
     * Default is the bean name of this FactoryBean.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the group of the job.
     * <p>
     * Default is the default group of the Scheduler.
     *
     * @see org.quartz.Scheduler#DEFAULT_GROUP
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Specify whether or not multiple jobs should be run in a concurrent
     * fashion. The behavior when one does not want concurrent jobs to be
     * executed is realized through adding the
     * {@code @PersistJobDataAfterExecution} and
     * {@code @DisallowConcurrentExecution} markers. More information on
     * stateful versus stateless jobs can be found <a href=
     * "http://www.quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-03"
     * > here</a>.
     * <p>
     * The default setting is to run jobs concurrently.
     */
    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    /**
     * Set the name of the target bean in the Spring BeanFactory.
     * <p>
     * This is an alternative to specifying {@link #setTargetObject
     * "targetObject"}, allowing for non-singleton beans to be invoked. Note
     * that specified "targetObject" and {@link #setTargetClass "targetClass"}
     * values will override the corresponding effect of this "targetBeanName"
     * setting (i.e. statically pre-define the bean type or even the bean
     * object).
     */
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

    /*
     * public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
     * this.jdbcTemplate = jdbcTemplate; }
     *
     * public void setTableName(String tableName) { this.tableName = tableName;
     * }
     */

    @Override
    protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
        return ClassUtils.forName(className, this.beanClassLoader);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException {
        prepare();

        /*
         * if (jdbcTemplate == null) { String[] names =
         * applicationContext.getBeanNamesForType(JdbcTemplate.class); if (names
         * != null && names.length > 0) { jdbcTemplate =
         * applicationContext.getBean(names[0], JdbcTemplate.class); } }
         *
         * if (jdbcTemplate == null) { String[] names =
         * applicationContext.getBeanNamesForType(DataSource.class); if (names
         * != null && names.length > 0) { DataSource dataSource =
         * applicationContext.getBean(names[0], DataSource.class); jdbcTemplate
         * = new JdbcTemplate(dataSource); } }
         *
         * Assert.notNull(jdbcTemplate, "jdbcTemplate must not be null");
         */

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

    /**
     * Callback for post-processing the JobDetail to be exposed by this
     * FactoryBean.
     * <p>
     * The default implementation is empty. Can be overridden in subclasses.
     *
     * @param jobDetail the JobDetail prepared by this FactoryBean
     */
    protected void postProcessJobDetail(JobDetail jobDetail) {
    }

    /**
     * Overridden to support the {@link #setTargetBeanName "targetBeanName"}
     * feature.
     */
    @Override
    public Class<?> getTargetClass() {
        Class<?> targetClass = super.getTargetClass();
        if (targetClass == null && this.targetBeanName != null) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set when using 'targetBeanName'");
            targetClass = this.beanFactory.getType(this.targetBeanName);
        }
        return targetClass;
    }

    /**
     * Overridden to support the {@link #setTargetBeanName "targetBeanName"}
     * feature.
     */
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

    /**
     * Quartz Job implementation that invokes a specified method. Automatically
     * applied by MethodInvokingJobDetailFactoryBean.
     */
    public static class MethodInvokingJob extends QuartzJobBean {

        protected static final Logger logger = LoggerFactory.getLogger(MethodInvokingJob.class);
        private MethodInvoker methodInvoker;

        // private JdbcTemplate jdbcTemplate;
        //
        // private String tableName;

        private boolean concurrent;

        /**
         * Set the MethodInvoker to use.
         */
        public void setMethodInvoker(MethodInvoker methodInvoker) {
            this.methodInvoker = methodInvoker;
        }

        /*
         * public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
         * this.jdbcTemplate = jdbcTemplate; }
         *
         * public void setTableName(String tableName) { this.tableName =
         * tableName; }
         */

        public void setConcurrent(boolean concurrent) {
            this.concurrent = concurrent;
        }

        /**
         * Invoke the method via the MethodInvoker.
         */
        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            // QuartzJobPersistence persistence = new
            // QuartzJobPersistence(jdbcTemplate);
            // Long id = persistence.createJob(context, tableName, concurrent);

            // JobStatus status = JobStatus.FAILED;
            JobKey key = context.getJobDetail().getKey();
            try {
                if (AppConfig.getInt("skip.all", 0) > 0) {
                    logger.info("skip.all > 0, 跳过定时任务:" + key.getGroup().toLowerCase() + "-" + key.getName());
                    // status = JobStatus.SKIP_ALL;
                    return;
                }
                if (AppConfig.getInt("run." + key.getGroup().toLowerCase() + "." + key.getName(), 1) <= 0) {
                    logger.info("run." + key.getGroup().toLowerCase() + "." + key.getName() + " <= 0,跳过定时任务:"
                            + key.getGroup().toLowerCase() + "-" + key.getName());
                    // status = JobStatus.SKIP;
                    return;
                }
                context.setResult(this.methodInvoker.invoke());
                // status = JobStatus.SUCCESSED;
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

    /**
     * Extension of the MethodInvokingJob, implementing the StatefulJob
     * interface. Quartz checks whether or not jobs are stateful and if so,
     * won't let jobs interfere with each other.
     */
    @PersistJobDataAfterExecution
    @DisallowConcurrentExecution
    public static class StatefulMethodInvokingJob extends MethodInvokingJob {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}