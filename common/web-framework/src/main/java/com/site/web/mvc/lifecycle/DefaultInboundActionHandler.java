package com.site.web.mvc.lifecycle;

import static com.site.lookup.util.ReflectUtils.createInstance;
import static com.site.lookup.util.ReflectUtils.invokeMethod;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.lookup.ContainerHolder;
import com.site.lookup.util.ReflectUtils;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionException;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.PayloadProvider;
import com.site.web.mvc.Validator;
import com.site.web.mvc.model.InboundActionModel;
import com.site.web.mvc.payload.DefaultPayloadProvider;
import com.site.web.mvc.payload.annotation.PayloadProviderMeta;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultInboundActionHandler extends ContainerHolder implements InboundActionHandler, LogEnabled {
   private InboundActionModel m_inboundAction;

	private Class<?> m_payloadClass;

	private PayloadProvider m_payloadProvider;

	private List<Validator<ActionContext>> m_validators;

	private Logger m_logger;

	public void handle(ActionContext ctx) throws ActionException {
		try {
			if (m_payloadClass != null) {
				RequestContext requestContext = ctx.getRequestContext();
				ActionPayload payload = createInstance(m_payloadClass);

				payload.setPage(requestContext.getAction());
				m_payloadProvider.process(requestContext.getUrlMapping(), requestContext.getParameterProvider(), payload);
				payload.validate(ctx);
				ctx.setPayload(payload);
			}

			for (Validator<ActionContext> validator : m_validators) {
				validator.validate(ctx);
			}

			invokeMethod(m_inboundAction.getActionMethod(), m_inboundAction.getModuleInstance(), ctx);
		} catch (Exception e) {
			throw new ActionException("Error occured during handling inbound action(" + m_inboundAction.getActionName() + ")", e);
		}
	}

	private PayloadProvider createPayloadProviderInstance(Class<? extends PayloadProvider> payloadProviderClass) {
		if (hasComponent(payloadProviderClass)) {
			return lookup(payloadProviderClass);
		} else {
			// create a POJO instance with default constructor
			return ReflectUtils.createInstance(payloadProviderClass);
		}
	}

	public void initialize(InboundActionModel inboundAction) {
		m_inboundAction = inboundAction;
		m_payloadClass = inboundAction.getPayloadClass();

		if (m_payloadClass != null) {
			PayloadProviderMeta providerMeta = m_payloadClass.getAnnotation(PayloadProviderMeta.class);

			if (providerMeta == null) {
				m_payloadProvider = createPayloadProviderInstance(DefaultPayloadProvider.class);
			} else {
				m_payloadProvider = createPayloadProviderInstance(providerMeta.value());
			}

			m_payloadProvider.register(m_payloadClass);
		}

		m_validators = new ArrayList<Validator<ActionContext>>();

		for (Class<?> validatorClass : inboundAction.getValidationClasses()) {
			Validator<ActionContext> validator = createInstance(validatorClass);

			m_validators.add(validator);
		}

		m_logger.debug(getClass().getSimpleName() + " initialized for  " + inboundAction.getActionName());
	}

	public void enableLogging(Logger logger) {
		m_logger = logger;
	}
}
