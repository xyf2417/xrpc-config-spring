package xyf.xrpc.config.schema.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import xyf.xrpc.config.ApplicationConfig;

public class ApplicationConfigBeanDefinitionParser implements
		BeanDefinitionParser {
	
	private final static Log logger = LogFactory.getLog(ApplicationConfigBeanDefinitionParser.class);
	
	private final static Class<?> BEAN_CLASS = ApplicationConfig.class;
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(BEAN_CLASS);
        beanDefinition.setLazyInit(false);
        
        //Parse the id of this bean.
        String id = element.getAttribute("id");
        
		if (id == null || id.length() <= 0) {
			id = BEAN_CLASS.getName() + "-" + System.currentTimeMillis();
		}
		
		if (parserContext.getRegistry().containsBeanDefinition(id)) {
			throw new IllegalStateException("Duplicate spring bean id: "
					+ id + " of " + BEAN_CLASS);
		}
		beanDefinition.getPropertyValues().addPropertyValue("id", id);
		
		//Parse the application name.
		String name = element.getAttribute("name");
		if (name == null || name.length() == 0) {
			name = id;
		}
		beanDefinition.getPropertyValues().addPropertyValue("applicationName", name);
		
		if(logger.isInfoEnabled())
        {
        	logger.info("xrpc: Registering the ApplicationConfig with id: " + id + " into the beanfactory");
        }
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        
        return beanDefinition;
	}

}
