package xyf.xrpc.config.schema.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class XrpcNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("application", new ApplicationConfigBeanDefinitionParser());
		registerBeanDefinitionParser("registry", new RegistryConfigBeanDefinitionParser());

	}

}
