package org.unidal.education;

import javax.swing.JButton;
import javax.swing.JPanel;

public class NumberModule extends BaseModule {
   private static final long serialVersionUID = -8641757815952431070L;

   public NumberModule(EducationApp app) {
      super(app);

      initialize();
   }

   private void initialize() {
      // Create the desktop pane
      JPanel panel = getPanel();

      JButton button1 = new JButton("10ÒÔÄÚ¼Ó¼õ");
      panel.add(button1);
   }

   /**
    * main method allows us to run as a standalone demo.
    */
   public static void main(String[] args) {
      NumberModule module = new NumberModule(null);

      module.mainImpl();
   }

}
