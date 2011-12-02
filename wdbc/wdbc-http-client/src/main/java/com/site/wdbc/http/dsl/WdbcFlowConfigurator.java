package com.site.wdbc.http.dsl;

import java.util.ArrayList;
import java.util.List;

import com.site.lookup.configuration.Component;
import com.site.lookup.configuration.Configuration;
import com.site.wdbc.FindQuery;
import com.site.wdbc.ResourceSource;
import com.site.wdbc.SelectQuery;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.dsl.AbstractConfigurator.Attribute;
import com.site.wdbc.http.dsl.AbstractConfigurator.BaseQuery;
import com.site.wdbc.http.dsl.AbstractConfigurator.Dependency;
import com.site.wdbc.http.dsl.AbstractConfigurator.Execution;
import com.site.wdbc.http.dsl.AbstractConfigurator.Field;
import com.site.wdbc.http.dsl.AbstractConfigurator.Filter;
import com.site.wdbc.http.dsl.AbstractConfigurator.Flow;
import com.site.wdbc.http.dsl.AbstractConfigurator.FormQuery;
import com.site.wdbc.http.dsl.AbstractConfigurator.Handler;
import com.site.wdbc.http.dsl.AbstractConfigurator.HandlerExecution;
import com.site.wdbc.http.dsl.AbstractConfigurator.LeafParam;
import com.site.wdbc.http.dsl.AbstractConfigurator.LocalQuery;
import com.site.wdbc.http.dsl.AbstractConfigurator.LoopQueryExecution;
import com.site.wdbc.http.dsl.AbstractConfigurator.NodeParam;
import com.site.wdbc.http.dsl.AbstractConfigurator.Param;
import com.site.wdbc.http.dsl.AbstractConfigurator.Processor;
import com.site.wdbc.http.dsl.AbstractConfigurator.ProcessorExecution;
import com.site.wdbc.http.dsl.AbstractConfigurator.Query;
import com.site.wdbc.http.dsl.AbstractConfigurator.QueryExecution;
import com.site.wdbc.http.dsl.AbstractConfigurator.QueryFilter;
import com.site.wdbc.http.dsl.AbstractConfigurator.QueryProcessor;
import com.site.wdbc.http.dsl.AbstractConfigurator.Requirement;
import com.site.wdbc.http.dsl.AbstractConfigurator.SampleField;
import com.site.wdbc.http.dsl.AbstractConfigurator.TrackingSupport;
import com.site.wdbc.http.dsl.AbstractConfigurator.Visitor;
import com.site.wdbc.http.impl.DefaultFlow;
import com.site.wdbc.http.impl.FormRequest;
import com.site.wdbc.query.WdbcFilter;

public class WdbcFlowConfigurator {
   public static WdbcFlowConfigurator with(AbstractConfigurator configurator) {
      WdbcFlowConfigurator instance = new WdbcFlowConfigurator(configurator);

      return instance;
   }

   private AbstractConfigurator m_configurator;

   private WdbcFlowConfigurator(AbstractConfigurator configurator) {
      m_configurator = configurator;
   }

   public List<FindQueryPlan> getFindQueryPlans() {
      List<FindQueryPlan> plans = new ArrayList<FindQueryPlan>();
      FindQueryVisitor visitor = new FindQueryVisitor(plans);

      m_configurator.accept(visitor);
      return plans;
   }

   public List<Component> getRuntimeComponents() {
      List<Component> components = new ArrayList<Component>();
      MainVisitor visitor = new MainVisitor(components);

      m_configurator.accept(visitor);
      return components;
   }

   public List<SelectQueryPlan> getSelectQueryPlans() {
      List<SelectQueryPlan> plans = new ArrayList<SelectQueryPlan>();
      SelectQueryVisitor visitor = new SelectQueryVisitor(plans);

      m_configurator.accept(visitor);
      return plans;
   }

   public static class FindQueryPlan {
      private String m_name;

      private WdbcQuery m_query;

      private WdbcSource m_source;

      public FindQueryPlan(String name) {
         m_name = name;
      }

      public String getName() {
         return m_name;
      }

      public WdbcQuery getQuery() {
         return m_query;
      }

      public WdbcSource getSource() {
         return m_source;
      }

      public void setQuery(WdbcQuery query) {
         m_query = query;
      }

      public void setSource(WdbcSource source) {
         m_source = source;
      }
   }

   public static class FindQueryVisitor extends TrackingSupport implements Visitor {
      private List<FindQueryPlan> m_plans;

      public FindQueryVisitor(List<FindQueryPlan> plans) {
         super(true);

         m_plans = plans;
      }

      public void visit(AbstractConfigurator configurator) {
         for (Query query : configurator.getQueries()) {
            query.accept(this);
         }
      }

      public void visitDependency(Dependency dependency) {
      }

      public void visitExecution(Execution execution) {
      }

      public void visitField(Field field) {
         if (field instanceof SampleField) {
            SampleField sampleField = (SampleField) field;
            String fieldName = field.getName();
            int index = fieldName.indexOf('@');
            String name = (index < 0 ? fieldName : fieldName.substring(0, index));
            String suffix = (index < 0 ? null : fieldName.substring(index));
            String[] samples = sampleField.getSamples();
            Configuration words = peek();

            for (String sample : samples) {
               Configuration word = new Configuration("word");

               word.setAttribute("name", name);
               word.setValue(sample + (suffix == null ? "" : suffix));

               words.add(word);
            }
         }
      }

      public void visitFilter(Filter filter) {
      }

      public void visitFlow(Flow flow) {
      }

      public void visitHandler(Handler handler) {
      }

      public void visitParam(Param param) {
      }

      public void visitProcessor(Processor processor) {
      }

      public void visitQuery(Query query) {
         if (query instanceof FormQuery) {
            FormQuery form = (FormQuery) query;
            String samplePage = form.getSamplePage();

            if (samplePage != null) {
               List<Field> fields = form.getFormFields();

               if (!fields.isEmpty()) {
                  FindQueryPlan plan = new FindQueryPlan(query.getName());
                  ResourceSource source = new ResourceSource(WdbcSourceType.HTML, samplePage, "utf-8");
                  FindQuery findQuery = new FindQuery();

                  m_plans.add(plan);
                  plan.setQuery(findQuery);
                  plan.setSource(source);

                  Configuration words = new Configuration("words");

                  push(words);

                  for (Field field : fields) {
                     field.accept(this);
                  }

                  pop();

                  findQuery.setName(query.getName());
                  findQuery.setWords(words);
               }
            }
         }
      }

      public void visitRequirement(Requirement requirement) {
      }
   }

   public static class MainVisitor extends TrackingSupport implements Visitor {
      private List<Component> m_all;

      public MainVisitor(List<Component> all) {
         super(true);

         m_all = all;
      }

      public void visit(AbstractConfigurator configurator) {
         for (Query query : configurator.getQueries()) {
            query.accept(this);
         }

         for (Flow flow : configurator.getFlows()) {
            flow.accept(this);
         }
      }

      public void visitDependency(Dependency dependency) {
         Component component = peek();

         component.req(dependency.getRole(), dependency.getRoleHint(), dependency.getField());
      }

      public void visitExecution(Execution execution) {
         Object parent = peek();

         if (execution instanceof HandlerExecution) {
            HandlerExecution e = (HandlerExecution) execution;
            Configuration handler = new Configuration("handler");

            if (parent instanceof Component) {
               Component wdbcFlow = (Component) parent;

               wdbcFlow.config(handler);
            } else if (parent instanceof Configuration) {
               Configuration configuration = (Configuration) parent;

               configuration.add(handler);
            }

            push(handler);
            e.getHandler().accept(this);
            pop();
         } else if (execution instanceof QueryExecution) {
            QueryExecution e = (QueryExecution) execution;
            Configuration page = new Configuration("page");

            page.setAttribute("name", e.getQuery().getName());
            if (parent instanceof Configuration) {
               Configuration configuration = (Configuration) parent;

               configuration.add(page);
            }
         } else if (execution instanceof LoopQueryExecution) {
            if (parent instanceof Configuration) {
               LoopQueryExecution e = (LoopQueryExecution) execution;
               Query query = e.getQuery();
               int interval = e.getQueryIntervalInSecond();
               Configuration configuration = (Configuration) parent;

               if (query instanceof FormQuery) {
                  Configuration page = new Configuration("page");

                  page.setAttribute("name", query.getName());
                  configuration.add(page);
               }

               Configuration handler = new Configuration("handler");

               handler.setAttribute("query", query.getName());

               if (interval > 0) {
                  handler.setAttribute("interval", String.valueOf(interval));
               }

               configuration.add(handler);

               push(handler);
               e.getHandler().accept(this);
               pop();
            }
         } else if (execution instanceof ProcessorExecution) {
            if (parent instanceof Configuration) {
               ProcessorExecution e = (ProcessorExecution) execution;
               Configuration handler = (Configuration) parent;
               
               push(handler);
               e.getProcessor().accept(this);
               pop();
            }
         }
      }

      public void visitField(Field field) {
         String fieldName = field.getName();
         int index = fieldName.indexOf('@');
         String name = (index < 0 ? fieldName : fieldName.substring(0, index));

         Configuration paths = peek();
         Configuration path = new Configuration("path");

         path.setAttribute("name", name);

         if (field instanceof SampleField) {
            SampleField sample = (SampleField) field;

            path.setValue(sample.getPath());
         } else {
            path.setValue("-");
         }

         paths.add(path);
      }

      public void visitFilter(Filter filter) {
         Component wdbcFilter = new Component(WdbcFilter.class, filter.getName(), filter.getFilterClass());

         m_all.add(wdbcFilter);

         // for filter
         push(wdbcFilter);

         if (filter instanceof QueryFilter) {
            QueryFilter queryFilter = (QueryFilter) filter;

            for (Param param : queryFilter.getParams()) {
               param.accept(this);
            }
         }

         for (Dependency dependency : filter.getDependencies()) {
            dependency.accept(this);
         }

         pop();
      }

      public void visitFlow(Flow flow) {
         Component wdbcFlow = new Component(com.site.wdbc.http.Flow.class, flow.getName(), DefaultFlow.class);

         m_all.add(wdbcFlow);

         // for execution
         push(wdbcFlow);

         for (Execution execution : flow.getExecutions()) {
            execution.accept(this);
         }

         pop();
      }

      public void visitHandler(Handler handler) {
         for (Execution execution : handler.getExecutions()) {
            execution.accept(this);
         }
      }

      public void visitParam(Param param) {
         Configuration child = null;

         if (param instanceof NodeParam) {
            NodeParam node = (NodeParam) param;

            child = new Configuration(param.getName());
            push(child);

            for (Param childParam : node.getParams()) {
               childParam.accept(this);
            }

            pop();
         } else if (param instanceof LeafParam) {
            LeafParam leaf = (LeafParam) param;

            child = new Configuration(param.getName());
            child.setValue(leaf.getValue());

            for (Attribute attr : leaf.getAttributes()) {
               child.setAttribute(attr.getName(), attr.getValue());
            }
         }

         if (child != null) {
            Object parent = peek();

            if (parent instanceof Component) {
               Component component = (Component) parent;

               component.config(child);
            } else if (parent instanceof Configuration) {
               Configuration configuration = (Configuration) parent;

               configuration.add(child);
            }
         }
      }

      public void visitProcessor(Processor processor) {
         Configuration configuration = peek();
         Configuration child = new Configuration("processor");

         child.setAttribute("name", processor.getName());
         configuration.add(child);

         Component wdbcProcessor = new Component(com.site.wdbc.http.Processor.class, processor.getName(), processor
               .getProcessorClass());

         m_all.add(wdbcProcessor);

         // for processor
         push(wdbcProcessor);

         if (processor instanceof QueryProcessor) {
            QueryProcessor queryProcessor = (QueryProcessor) processor;

            for (Param param : queryProcessor.getParams()) {
               param.accept(this);
            }
         }

         for (Dependency dependency : processor.getDependencies()) {
            dependency.accept(this);
         }

         pop();
      }

      public void visitQuery(Query query) {
         Component wdbcRequest = new Component(Request.class, query.getName(), FormRequest.class);
         Component wdbcQuery = new Component(WdbcQuery.class, query.getName(), SelectQuery.class);

         if (query instanceof FormQuery) {
            FormQuery form = (FormQuery) query;

            m_all.add(wdbcRequest);

            if (!form.getFormFields().isEmpty()) {
               m_all.add(wdbcQuery);
            }
         } else if (query instanceof LocalQuery) {
            m_all.add(wdbcQuery);
         }

         if (query instanceof BaseQuery) {
            BaseQuery<?> base = (BaseQuery<?>) query;

            // for form request
            push(wdbcRequest);

            for (Param param : base.getFormParams()) {
               param.accept(this);
            }

            List<Field> fields = base.getFormFields();

            if (!fields.isEmpty()) {
               Configuration paths = new Configuration("paths");

               push(paths);

               for (Field field : fields) {
                  field.accept(this);
               }

               pop();
               wdbcQuery.config(paths);
            }

            for (Dependency dependency : base.getFormDependencies()) {
               dependency.accept(this);
            }

            pop();

            // for query
            push(wdbcQuery);

            for (Param param : base.getParams()) {
               param.accept(this);
            }

            QueryFilter filter = base.getFilter();

            if (filter != null) {
               Dependency filterDependency = new Dependency(WdbcFilter.class, query.getName());

               filterDependency.accept(this);
               filter.accept(this);
            }

            for (Dependency dependency : base.getDependencies()) {
               dependency.accept(this);
            }

            pop();
         }
      }

      public void visitRequirement(Requirement requirement) {
         for (Dependency dependency : requirement.getDependencies()) {
            dependency.accept(this);
         }
      }
   }

   public static class SelectQueryPlan {
      private String m_name;

      private WdbcSource m_source;

      public SelectQueryPlan(String name) {
         m_name = name;
      }

      public String getName() {
         return m_name;
      }

      public WdbcSource getSource() {
         return m_source;
      }

      public void setSource(WdbcSource source) {
         m_source = source;
      }
   }

   public static class SelectQueryVisitor extends TrackingSupport implements Visitor {
      private List<SelectQueryPlan> m_plans;

      public SelectQueryVisitor(List<SelectQueryPlan> plans) {
         super(true);

         m_plans = plans;
      }

      public void visit(AbstractConfigurator configurator) {
         for (Query query : configurator.getQueries()) {
            query.accept(this);
         }
      }

      public void visitDependency(Dependency dependency) {
      }

      public void visitExecution(Execution execution) {
      }

      public void visitField(Field field) {
      }

      public void visitFilter(Filter filter) {
      }

      public void visitFlow(Flow flow) {
      }

      public void visitHandler(Handler handler) {
      }

      public void visitParam(Param param) {
      }

      public void visitProcessor(Processor processor) {
      }

      public void visitQuery(Query query) {
         if (query instanceof FormQuery) {
            FormQuery form = (FormQuery) query;
            String samplePage = form.getSamplePage();

            if (samplePage != null) {
               List<Field> fields = form.getFormFields();

               if (!fields.isEmpty()) {
                  SelectQueryPlan plan = new SelectQueryPlan(query.getName());
                  ResourceSource source = new ResourceSource(WdbcSourceType.HTML, samplePage, "utf-8");

                  plan.setSource(source);
                  m_plans.add(plan);
               }
            }
         }
      }

      public void visitRequirement(Requirement requirement) {
      }
   }
}
