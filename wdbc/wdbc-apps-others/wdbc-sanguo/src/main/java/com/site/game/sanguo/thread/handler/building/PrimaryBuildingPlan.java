package com.site.game.sanguo.thread.handler.building;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class PrimaryBuildingPlan extends AbstractBuildingPlan implements Initializable {
   public void initialize() throws InitializationException {
      build("�о���", 1);
      build("����", 3);
      build("����", 1);
      build("У��", 5);
      build("��ľ˾", 10);
      build("�о���", 5);
      build("���͹�", 1);
      build("����", 10);
      build("�ֿ�", 10);
      build("��ۡ", 10);
      build("��ľ˾", 20);
      build("����", 15);
      build("ѧ��", 10);
      build("����", 20);
      build("�ֿ�", 20);
      build("����˾", 10);
      build("ұ����", 10);
      build("��", 10);
      build("���Ӫ", 10);
      build("У��", 10);
      build("����˾", 15);
      build("ұ����", 15);
      build("��", 15);
      build("���Ӫ", 15);
      build("У��", 20);
      build("�о���", 20);
      build("����Ӫ", 10);
   }
}
