package com.revature.rphVertical;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Interceptor implements BeanPostProcessor {
	@Autowired
	AutowireCapableBeanFactory beanFactory;
	@Autowired
	EntityManagerFactory emf;
	public Interceptor() {
		System.out.println("CustomBeanPostProcessor - CustomBeanPostProcessor.new invoked");
	}
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		if (bean.getClass().getDeclaredAnnotation(Service.class) != null)
			System.out.println(beanName);

		if (bean.getClass().getDeclaredAnnotation(Entity.class) == null)
			return bean;

		EntityManager entityManager = emf.createEntityManager();

		System.out.println("Creating Controller "+bean.getClass());

		System.out.println(entityManager);
		TypeDescription.Generic genericService =
			TypeDescription.Generic.Builder
				.parameterizedType(InterceptService.class,
					bean.getClass(),
					Arrays.stream(
						bean.getClass()
							.getDeclaredFields())
						.filter(x->x.isAnnotationPresent(Id.class))
						.findFirst()
						.get()
						.getClass())
				.build();

		Class<InterceptService> service = (Class<InterceptService>) new ByteBuddy()
			.subclass(genericService)
			.annotateType(
				AnnotationDescription.Builder.ofType(Service.class).build(),
				AnnotationDescription.Builder.ofType(Transactional.class).build())
			.make()
			.load(getClass().getClassLoader())
			.getLoaded();

		BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

		JpaEntityInformation<?,?> jpaEntityInformation = JpaEntityInformationSupport.getEntityInformation(bean.getClass(),entityManager);

		SimpleJpaRepository<?,?> repository = new SimpleJpaRepository<>(jpaEntityInformation,entityManager);

		RootBeanDefinition beanDefinition = new RootBeanDefinition( service);

		registry.registerBeanDefinition(beanName+"Service", beanDefinition);

		InterceptService s = (InterceptService) (beanFactory.getBean(beanName+"Service"));

		s.setRepo(repository);

		s.setEntityManager(entityManager);

		s.settClass(bean.getClass());

		TypeDescription.Generic genericController = TypeDescription.Generic.Builder.parameterizedType(InterceptController.class,bean.getClass()).build();

		Class<InterceptController> controller = (Class<InterceptController>) new ByteBuddy()
			.subclass(genericController)
			.annotateType(AnnotationDescription.Builder.ofType(RestController.class).build())
			.annotateType(AnnotationDescription.Builder.ofType(RequestMapping.class).defineArray("path","/"+beanName).build())
			.make()
			.load(getClass().getClassLoader())
			.getLoaded();

		System.out.println(Arrays.toString(controller.getDeclaredAnnotations()));
		beanDefinition = new RootBeanDefinition( controller);

		registry.registerBeanDefinition(beanName+"Controller", beanDefinition);

		InterceptController c = (InterceptController) (beanFactory.getBean(beanName+"Controller"));

		c.settClass(bean.getClass());

		c.setService(s);

		return bean;
	}

}
