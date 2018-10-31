package xyf.xrpc.config.schema.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xyf.xrpc.config.RegistryConfig;

public class RegistryConfigBeanDefinitionParser implements BeanDefinitionParser {
	
	private final static Log logger = LogFactory.getLog(RegistryConfigBeanDefinitionParser.class);
	
	private final static Class<?> BEAN_CLASS = RegistryConfig.class;
	
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(BEAN_CLASS);
        beanDefinition.setLazyInit(false);
        
        //Parse id.
        String id = element.getAttribute("id");
		if (id == null || id.length() <= 0) {
			id = BEAN_CLASS.getName() + "-" + System.currentTimeMillis();
		}
		if (parserContext.getRegistry().containsBeanDefinition(id)) {
			throw new IllegalStateException("Duplicate spring bean id "
					+ id + " of " + BEAN_CLASS);
		}
	
		beanDefinition.getPropertyValues().addPropertyValue("id", id);
	
		//Parse type
        String type = element.getAttribute("type");
        beanDefinition.getPropertyValues().addPropertyValue("type", type);
        
        ManagedList list = new ManagedList();
        
        NodeList addressesNodeLst = element.getChildNodes();
        for(int i=0; i< addressesNodeLst.getLength();i++)
        {
        	Node node = addressesNodeLst.item(i);
        	if(node.getNodeType() != Node.TEXT_NODE)
        	{
        		NodeList addressNodeLst = node.getChildNodes();
        		for(int j=0; j < addressNodeLst.getLength(); j++) {
        			Node addressNode = addressNodeLst.item(j);
        			if(addressNode != null && addressNode.getNodeType() != Node.TEXT_NODE) {
        				NamedNodeMap addressAttrMap = addressNode.getAttributes();
        				list.add(addressAttrMap.getNamedItem("host").toString().split("=")[1].replace("\"", "") + ":" + addressAttrMap.getNamedItem("port").toString().split("=")[1].replace("\"", ""));
        			}
        		}
        	}
        }
        beanDefinition.getPropertyValues().addPropertyValue("addresses", list);
        
        //TODO: valid the address format
        
        if(logger.isInfoEnabled())
        {
        	logger.info("frpc: Registering the Registry with id='" + id + "' into the beanfactory");
        }
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
		return beanDefinition;
	}

}
