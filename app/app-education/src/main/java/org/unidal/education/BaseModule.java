package org.unidal.education;

/*
 * @(#)DemoModule.java	1.23 05/11/17
 * 
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

/*
 * @(#)DemoModule.java	1.23 05/11/17
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * A generic SwingSet2 demo module
 * 
 * @version 1.23 11/17/05
 * @author Jeff Dinkins
 */
public class BaseModule extends JApplet {
   private static final long serialVersionUID = 1L;
   
   // The preferred size of the demo
   private int PREFERRED_WIDTH = 680;
   private int PREFERRED_HEIGHT = 600;

   Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(5, 5, 5, 5));

   // Premade convenience dimensions, for use wherever you need 'em.
   public static Dimension HGAP2 = new Dimension(2, 1);
   public static Dimension VGAP2 = new Dimension(1, 2);

   public static Dimension HGAP5 = new Dimension(5, 1);
   public static Dimension VGAP5 = new Dimension(1, 5);

   public static Dimension HGAP10 = new Dimension(10, 1);
   public static Dimension VGAP10 = new Dimension(1, 10);

   public static Dimension HGAP15 = new Dimension(15, 1);
   public static Dimension VGAP15 = new Dimension(1, 15);

   public static Dimension HGAP20 = new Dimension(20, 1);
   public static Dimension VGAP20 = new Dimension(1, 20);

   public static Dimension HGAP25 = new Dimension(25, 1);
   public static Dimension VGAP25 = new Dimension(1, 25);

   public static Dimension HGAP30 = new Dimension(30, 1);
   public static Dimension VGAP30 = new Dimension(1, 30);

   private EducationApp swingset = null;
   private JPanel panel = null;
   private String resourceName = null;
   private String iconPath = null;
   private String sourceCode = null;

   // Resource bundle for internationalized and accessible text
   private ResourceBundle bundle = null;

   public BaseModule(EducationApp swingset) {
      this(swingset, null, null);
   }

   public BaseModule(EducationApp swingset, String resourceName, String iconPath) {
      UIManager.put("swing.boldMetal", Boolean.FALSE);
      panel = new JPanel();
      panel.setLayout(new BorderLayout());

      this.resourceName = resourceName;
      this.iconPath = iconPath;
      this.swingset = swingset;
   }

   public String getResourceName() {
      return resourceName;
   }

   public JPanel getPanel() {
      return panel;
   }

   public EducationApp getApp() {
      return swingset;
   }

   public String getString(String key) {
      String value = "nada";
      if (bundle == null) {
         if (getApp() != null) {
            bundle = getApp().getResourceBundle();
         } else {
            bundle = ResourceBundle.getBundle("resources.swingset");
         }
      }
      try {
         value = bundle.getString(key);
      } catch (MissingResourceException e) {
         System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
      }
      return value;
   }

   public char getMnemonic(String key) {
      return (getString(key)).charAt(0);
   }

   public ImageIcon createImageIcon(String filename, String description) {
      if (getApp() != null) {
         return getApp().createImageIcon(filename, description);
      } else {
         String path = "/resources/images/" + filename;
         return new ImageIcon(getClass().getResource(path), description);
      }
   }

   public String getSourceCode() {
      return sourceCode;
   }

   public String getName() {
      return getString(getResourceName() + ".name");
   };

   public Icon getIcon() {
      return createImageIcon(iconPath, getResourceName() + ".name");
   };

   public String getToolTip() {
      return getString(getResourceName() + ".tooltip");
   };

   public void mainImpl() {
      JFrame frame = new JFrame(getName());
      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add(getPanel(), BorderLayout.CENTER);
      getPanel().setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
      frame.pack();
      frame.setVisible(true);
   }

   public JPanel createHorizontalPanel(boolean threeD) {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
      p.setAlignmentY(TOP_ALIGNMENT);
      p.setAlignmentX(LEFT_ALIGNMENT);
      if (threeD) {
         p.setBorder(loweredBorder);
      }
      return p;
   }

   public JPanel createVerticalPanel(boolean threeD) {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      p.setAlignmentY(TOP_ALIGNMENT);
      p.setAlignmentX(LEFT_ALIGNMENT);
      if (threeD) {
         p.setBorder(loweredBorder);
      }
      return p;
   }

   public static void main(String[] args) {
      BaseModule demo = new BaseModule(null);
      demo.mainImpl();
   }

   public void init() {
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(getPanel(), BorderLayout.CENTER);
   }

   void updateDragEnabled(boolean dragEnabled) {
   }
}
