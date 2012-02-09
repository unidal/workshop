package com.site.maven.plugin.webres.rule;

import java.lang.reflect.AnnotatedElement;

public interface IRuleExemptionHandler {
   public void onExemption(AnnotatedElement source, AnnotatedElement target, RuleExemption exemption);
}
