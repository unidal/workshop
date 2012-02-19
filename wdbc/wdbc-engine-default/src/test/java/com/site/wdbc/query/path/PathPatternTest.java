package com.site.wdbc.query.path;

import static com.site.wdbc.query.path.WdbcPathPattern.FULL_MATCHED;
import static com.site.wdbc.query.path.WdbcPathPattern.FULL_WILD_MATCHED;
import static com.site.wdbc.query.path.WdbcPathPattern.NOT_MATCHED;
import static com.site.wdbc.query.path.WdbcPathPattern.PARTIAL_WILD_MATCHED;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.query.DefaultWdbcContext;
import com.site.wdbc.query.WdbcContext;

public class PathPatternTest extends ComponentTestCase {
   private WdbcContext getContextComplex1(PathPattern[] patterns) {
      WdbcContext context = new DefaultWdbcContext();
      context.setTagTree(TagTree.buildTree(Arrays.asList(patterns), true));

      context.push("t1", null);
      context.pop("t1");
      context.push("t1", null);
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.pop("t2");
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);

      return context;
   }

   private WdbcContext getContextComplex2(PathPattern[] patterns) {
      WdbcContext context = new DefaultWdbcContext();
      context.setTagTree(TagTree.buildTree(Arrays.asList(patterns), true));

      context.push("t1", null);
      context.pop("t1");
      context.push("t0", null);
      context.pop("t0");
      context.push("t1", null);
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.pop("t2");
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t4", null);
      context.pop("t4");
      context.push("t3", null);

      return context;
   }

   private WdbcContext getContextComplex3(PathPattern[] patterns) {
      WdbcContext context = new DefaultWdbcContext();
      context.setTagTree(TagTree.buildTree(Arrays.asList(patterns), true));

      context.push("t1", null);
      context.pop("t1");
      context.push("t0", null);
      context.pop("t0");
      context.push("t1", null);
      context.push("t1", null);
      context.pop("t1");
      context.push("t1", null);
      context.pop("t1");
      context.push("t1", null);
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.pop("t2");
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t4", null);
      context.pop("t4");
      context.push("t3", null);

      return context;
   }

   private WdbcContext getContextComplex4(PathPattern[] patterns) {
      WdbcContext context = new DefaultWdbcContext();
      context.setTagTree(TagTree.buildTree(Arrays.asList(patterns), true));

      context.push("t1", null);
      context.setText("t1 text");
      context.push("t2", null);
      context.setText("t2 text 1");
      context.pop("t2");
      context.push("t2", null);
      context.setText("t2 text 2");
      context.push("t3", null);
      context.setText("t3 text 1");
      context.pop("t3");
      context.push("t3", null);
      context.setText("t3 text 2");
      context.pop("t3");
      context.push("t3", null);
      context.setText("t3 text 3");
      context.pop("t3");
      context.push("t4", null);
      context.setText("t2 text 1");
      context.pop("t4");
      context.push("t3", null);
      context.setText("t3 text 4");

      return context;
   }

   private WdbcContext getContextMiddle1(PathPattern[] patterns) {
      WdbcContext context = new DefaultWdbcContext();
      context.setTagTree(TagTree.buildTree(Arrays.asList(patterns), true));

      context.push("t1", null);
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);

      return context;
   }

   private WdbcContext getContextMiddle2(PathPattern[] patterns) {
      WdbcContext context = new DefaultWdbcContext();
      context.setTagTree(TagTree.buildTree(Arrays.asList(patterns), true));

      context.push("t1", null);
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.pop("t2");
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);

      return context;
   }

   private WdbcContext getContextMiddle3(PathPattern[] patterns) {
      WdbcContext context = new DefaultWdbcContext();
      context.setTagTree(TagTree.buildTree(Arrays.asList(patterns), true));

      context.push("t1", null);
      context.push("t2", map("class", "abc"));
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);
      context.pop("t3");
      context.pop("t2");
      context.push("t2", null);
      context.push("t3", null);
      context.pop("t3");
      context.push("t3", null);

      return context;
   }

   private Map<String, String> map(String... keyValuePairs) {
      Map<String, String> map = new HashMap<String, String>();

      for (int i = 0; i < keyValuePairs.length; i += 2) {
         map.put(keyValuePairs[i], keyValuePairs[i + 1]);
      }

      return map;
   }

   private WdbcContext getContextSimple(PathPattern[] patterns) {
      WdbcContext context = new DefaultWdbcContext();
      context.setTagTree(TagTree.buildTree(Arrays.asList(patterns), true));

      context.push("t1", null);
      context.push("t2", null);
      context.push("t3", null);

      return context;
   }

   public void testMatchesComplex1() {
      PathPattern[] patterns = new PathPattern[] {

      new PathPattern("", "t1[2].t2[*].t3[2]"), //
            new PathPattern("", "t1[2].t2[*]"), //
            new PathPattern("", "t1[2].t2[*].t3"), //
            new PathPattern("", "t1[2].t2[*].t3[3]"), //
      };
      WdbcContext ctx = getContextComplex1(patterns);

      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[0]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[1]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[2]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[3]));
   }

   public void testMatchesComplex2() {
      PathPattern[] patterns = new PathPattern[] { new PathPattern("", "t1.t2[*].t3"), //
            new PathPattern("", "t1.t2[*].t3[2]"), //
            new PathPattern("", "t1[2].t2[*].t3"), //
            new PathPattern("", "t1[2].t2[*].t3[2]"), //
      };
      WdbcContext ctx = getContextComplex2(patterns);

      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[0]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[1]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[2]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[3]));
   }

   public void testMatchesComplex3() {
      PathPattern[] patterns = new PathPattern[] { //
      new PathPattern("", "t1.t2[*].t3"), //
            new PathPattern("", "t1.t2[*].t3[2]"), //
            new PathPattern("", "t1[2].t2[*].t3"), //
            new PathPattern("", "t1[2].t2[*].t3[2]"), //
            new PathPattern("", "t1[2].t1.t2[*].t3[2]"), //
            new PathPattern("", "t1[2].t1[3].t2[*].t3[2]"), //
            new PathPattern("", "t1[2].t1[3].t3[2]"), //
            new PathPattern("", "t3[2]"), //
      };
      WdbcContext ctx = getContextComplex3(patterns);

      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[0]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[1]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[2]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[3]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[4]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[5]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[6]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[7]));
   }

   public void testMatchesComplex4() {
      PathPattern[] patterns = new PathPattern[] { //
      new PathPattern("", "t1.#text"), //
            new PathPattern("", "t1.*text"), //
            new PathPattern("", "t1.t2[2].*text"), //
            new PathPattern("", "t1.t2[2].t3[4].*text"), //
      };
      WdbcContext ctx = getContextComplex4(patterns);

      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[0]));
      assertEquals(PARTIAL_WILD_MATCHED, ctx.matchesPath(patterns[1]));
      assertEquals(PARTIAL_WILD_MATCHED, ctx.matchesPath(patterns[2]));
      assertEquals(FULL_WILD_MATCHED, ctx.matchesPath(patterns[3]));
   }

   public void testMatchesMiddle1() {
      PathPattern[] patterns = new PathPattern[] { new PathPattern("", "t1.t2[*].t3"),
            new PathPattern("", "t1.t2[*].t3[2]"), new PathPattern("", "t1.t2[*].t3[3]"), };
      WdbcContext ctx = getContextMiddle1(patterns);

      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[0]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[1]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[2]));
   }

   public void testMatchesMiddle2() {
      PathPattern[] patterns = new PathPattern[] { new PathPattern("", "t1.t2[*].t3"),
            new PathPattern("", "t1.t2[*].t3[2]"), new PathPattern("", "t1.t2[*].t3[3]"), };
      WdbcContext ctx = getContextMiddle2(patterns);

      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[0]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[1]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[2]));
   }

   public void testMatchesMiddle3() {
      PathPattern[] patterns = new PathPattern[] {//
      new PathPattern("", "t1.t2[@class=abc].t3"), //
            new PathPattern("", "t1.t2[*].t3[2]"), //
            new PathPattern("", "t1.t2[*].t3[3]"), //
      };
      WdbcContext ctx = getContextMiddle3(patterns);

      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[0], map("class", "abc")));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[1]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[2]));
   }

   public void testMatchesSimple() {
      PathPattern[] patterns = new PathPattern[] { new PathPattern("", "t1"), new PathPattern("", "t1.t2"),
            new PathPattern("", "t1.t2.t3"), new PathPattern("", "t2"), new PathPattern("", "t2.t3"),
            new PathPattern("", "t3"), new PathPattern("", "t4"), new PathPattern("", "t4.t2"), };
      WdbcContext ctx = getContextSimple(patterns);

      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[0]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[1]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[2]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[3]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[4]));
      assertEquals(FULL_MATCHED, ctx.matchesPath(patterns[5]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[6]));
      assertEquals(NOT_MATCHED, ctx.matchesPath(patterns[7]));
   }
}
