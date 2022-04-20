package thesis.webcryptoexchange;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import thesis.webcryptoexchange.config.*;

public class ServerInitializer extends AbstractAnnotationConfigDispatcherServletInitializer  {

	@Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{WebConf.class,ServerInitializer.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}