package modules.trackcontroller;

import java.util.*;
import shared.*;

public class TrackController {
     ArrayList<Double> speeds = new ArrayList<Double>();
     ArrayList<Double> authority = new ArrayList<Double>();
     int crossInd = -1, id;
     public MessageQueue mq = new MessageQueue();
     public PLC plcCode = new PLC();
     private Stack<Message> messages;
     TrackControllerGUI gui;
     NoCrossTCGUI ngui;
     private Message m;
     boolean[] mainLine, side, msplit, temp;
     Boolean recSwitch = null, switchPos = true, oneWay = null, loop = false, lights = true, priority = true, switchBias = true;
     boolean mainDir = false, sideDir = true, msplitDir = true, mainZero = true, sideZero = true, msplitZero = true, mode = true, crossExists = false, ctcSwitchPos, ctcswitch = false;
     boolean mainCross, sideCross, msplitCross, mainOcc, sideOcc, msplitOcc, crossPos = false, prevMainOcc, prevMsplitOcc, prevSideOcc, prevMainOcc2, prevMsplitOcc2, prevSideOcc2;
     boolean crossLights = false, switchLight = true, onSwitch = false, zeroSpeedSent = false, switchBias1, switchBias2, loop1, loop2, priority1, priority2, lights1, lights2, oneWay1, oneWay2;

     public TrackController(MessageQueue y, boolean[] n, boolean[] r, boolean[] s, int z, PLC p){
          id = z;
          mq = y;
          mainLine = n;
          msplit = r;
          side = s;
          plcCode = p;

		   for(int i=0; i<n.length; i++){
		   if(mainLine[i] == true){
				mainCross = true;
				crossInd = i;
				mainLine[i] = false;
                    crossExists = true;
		   }
          }
          for(int i=0; i<r.length; i++){
               if(msplit[i] == true){
                    msplitCross = true;
                    crossInd = i;
                    msplit[i] = false;
                    crossExists = true;
               }
          }
          for(int i=0; i<s.length; i++){
               if(side[i] == true){
                    sideCross = true;
                    crossInd = i;
                    side[i] = false;
                    crossExists = true;
               }
          }
		  if(crossExists){
			  gui = new TrackControllerGUI(this, n, r, s, z + 1, p);
			  gui.setVisible(true);
		  }
		  else{
			  ngui = new NoCrossTCGUI(this, n, r, s, z+1, p);
                 ngui.setVisible(true);
		  }
          checkZero();
     }
     public void run(){
		 ctcswitch = false;
          mReceive();
			mainOcc = getOcc(mainLine);
			prevMainOcc = getOcc(mainLine);
			prevMainOcc2 = getOcc(mainLine);
			sideOcc = getOcc(side);
			prevSideOcc = getOcc(side);
			prevSideOcc2 = getOcc(side);
			msplitOcc = getOcc(msplit);
			prevMsplitOcc = getOcc(msplit);
			prevMsplitOcc2 = getOcc(msplit);
			if(mainOcc != prevMainOcc || mainOcc != prevMainOcc2 || prevMainOcc != prevMainOcc2){		// Checks the occupancy 3 times to insure vital nature through redundancy
				panicMain();
			}
			if(sideOcc != prevSideOcc || sideOcc != prevSideOcc2 || prevSideOcc != prevSideOcc2){
				panicSide();
			}
			if(msplitOcc != prevMsplitOcc || msplitOcc != prevMsplitOcc2 || prevMsplitOcc != prevMsplitOcc2){
				panicMsplit();
			}
          getDir();
          if(mainLine[mainLine.length-1])
               onSwitch = true;
          else
               onSwitch = false;
          if(!mode){

          }
          else{
			  if(ctcswitch && !onSwitch){
				  switchPos = ctcSwitchPos;
			  }
              else if(!onSwitch)
                    logic();
               checkLights();
			   for(int i=0; i<3; i++){ 	//redundancy for vital nature
					if(crossExists)
						checkCross();
					checkContinue();
				}
			}
		   
          setGUI();
		  //System.out.println("zerospeed = " +zeroSpeedSent);
          mSend();
     }
     public void manualMode(){
          mode = false;
     }
     public void automaticMode(){
          mode = true;
     }
     public void plcImported(){
          switchBias = plcCode.getSwitchBias();
          switchBias1 = plcCode.getSwitchBias();
          switchBias2 = plcCode.getSwitchBias();
		  if(!(switchBias == switchBias1 == switchBias2))
			  System.out.println("PLC ERROR IN SWITCHBIAS");
          loop = plcCode.getLoop();
          loop1 = plcCode.getLoop();
          loop2 = plcCode.getLoop();
		  if(!(loop == loop1 == loop2))
			  System.out.println("PLC ERROR IN LOOP");
          priority = plcCode.getPriority();
          priority1 = plcCode.getPriority();
          priority2 = plcCode.getPriority();
		  if(!(priority == priority1 == priority2))
			  System.out.println("PLC ERROR IN PRIORITY");
          lights = plcCode.getLights();
          lights1 = plcCode.getLights();
          lights2 = plcCode.getLights();
		  if(!(lights == lights1 == lights2))
			  System.out.println("PLC ERROR IN LIGHTS");
          oneWay = plcCode.getOneWay();
          oneWay1 = plcCode.getOneWay();
          oneWay1 = plcCode.getOneWay();
		  if(oneWay!=null && !(oneWay == oneWay1 == oneWay2))
			  System.out.println("PLC ERROR IN ONEWAY");
     }
     public void mReceive(){
          messages = mq.receive(MDest.TcCtl+id);
          while(!messages.isEmpty()){
               m = messages.pop();
               if(m.type() == MType.AUTH){
				   mq.send(m, MDest.TcMd);
                    //authority.add(m.dataD());
               }
               else if(m.type() == MType.SPEED){
				   mq.send(m, MDest.TcMd);
                    //speeds.add(m.dataD());
                    //System.out.println("TkCon: "+m.dataD());
               }
               else if(m.type() == MType.TRACK){
                    boolean[] track = m.dataBA();
                    for(int i=0; i<track.length; i++){
                         if(i<mainLine.length){
                              mainLine[i] = track[i];
                         }
                         else if(i<(mainLine.length+msplit.length)){
                              msplit[i-mainLine.length] = track[i];
                         }
                         else if(i<(mainLine.length+msplit.length+side.length)){
                              side[i-mainLine.length-msplit.length] = track[i];
                         }
                    }
               }
               else if(m.type() == MType.REALTRACK){
                    boolean[] rTrack = m.dataBA();
                    m = new Message((MDest.TcCtl+id), rTrack, MType.REALTRACK);
                    mq.send(m, MDest.CTC);
               }
               else if(m.type() == MType.SWITCH){
                    recSwitch = m.dataB();
               }
               else if(m.type() == MType.MAINTENANCE){
                    int index = m.dataI();
                    m = new Message((MDest.TcCtl+id), index, MType.MAINTENANCE);
                    mq.send(m, MDest.TcMd);
               }
			   else if(m.type() == MType.CTCSWITCH){
				   ctcSwitchPos = m.dataB();
				   ctcswitch = true;
			   }
          }
     }
     public void mSend(){
          for(int i=0; i<speeds.size(); i++){
               m = new Message((MDest.TcCtl+id), speeds.get(i), MType.SPEED);
               //System.out.println("TcCtl"+id+": "+speeds.get(i));
               mq.send(m, MDest.TcMd);
               speeds.remove(i);
          }
          for(int i=0; i<authority.size(); i++){
               m = new Message((MDest.TcCtl+id), authority.get(i), MType.AUTH);
               mq.send(m, MDest.TcMd);
               authority.remove(i);
          }

          m = new Message((MDest.TcCtl+id), switchPos, MType.SWITCH);
          //System.out.println("SWITCH_FROM:"+m.dataB());
          mq.send(m, MDest.TcMd);
     }
     public void logic(){
          if(msplitOcc && !sideOcc && !mainOcc){
               if(!msplitDir){
                    switchPos = true;
               }
          }
          else if(sideOcc && !msplitOcc && !mainOcc){
               if(!sideDir){
                    switchPos = false;
               }
          }
          else if(mainOcc && !sideOcc && !msplitOcc){
               if(mainDir){
                    if(oneWay != null){
                         switchPos = oneWay;
                    }
                    else if(recSwitch != null){
                         switchPos = recSwitch;
                    }
                    else if(loop){
                              switchPos = true;
                    }
                    else
                         switchPos = switchBias;
               }
          }
          else if(sideOcc && mainOcc && !msplitOcc){
               if(sideDir && mainDir){
                    if(oneWay != null){
                         switchPos = oneWay;
                    }
                    else if(recSwitch != null){
                         switchPos = recSwitch;
                    }
                    else if(loop){
                              switchPos = true;
                    }
                    else
                         switchPos = switchBias;
               }
               else if(!sideDir && mainDir){
                    switchPos = true;
               }
               else if(!sideDir && !mainDir){
                    switchPos = false;
               }
          }
          else if(!sideOcc && mainOcc && msplitOcc){
               if(msplitDir && mainDir){
                    if(oneWay != null){
                         switchPos = oneWay;
                    }
                    else if(recSwitch != null){
                         switchPos = recSwitch;
                    }
                    else if(loop){
                              switchPos = true;
                    }
                    else
                         switchPos = switchBias;
               }
               else if(!msplitDir && mainDir){
                    switchPos = false;
               }
               else if(!msplitDir && !mainDir){
                    switchPos = true;
               }
          }
          else if(sideOcc && msplitOcc && !mainOcc){
               if(!msplitDir && !sideDir){
                    boolean closer = checkDist();
                    if(closer){
                         zeroSpeedSide();
						 //System.out.println("closer");
                         switchPos = true;
                    }
                    else{
                         zeroSpeedMsplit(); //msplit
						 //System.out.println("msplit");
                         switchPos = false;
                    }
               }
               else if(!msplitDir && sideDir){
                    switchPos = true;
               }
               else if(msplitDir && !sideDir){
                    switchPos = false;
               }
          }
          else if(mainOcc && msplitOcc && sideOcc){
               if(!msplitDir && !sideDir && !mainDir){
                    boolean closer = checkDist(); // look at priority in checkdist
                    if(closer){
                         zeroSpeedSide();
						 //System.out.println("closer2");
                         switchPos = true;
                    }
                    else{
                         zeroSpeedMsplit();
						 //System.out.println("msplit2");
                         switchPos = false;
                    }
               }
               else if(!msplitDir && sideDir && !mainDir){
                    switchPos = true;
               }
               else if(msplitDir && !sideDir && !mainDir){
                    switchPos = false;
               }
               else if(!msplitDir && sideDir && mainDir){
                    switchPos = false;
               }
               else if(msplitDir && !sideDir && mainDir){
                    switchPos = true;
               }
               else if(!msplitDir && !sideDir && mainDir){
                    panic();
               }
          }
		  else if(!mainOcc && !msplitOcc && !sideOcc){
			  switchPos = switchBias;
		  }
     }
     public boolean getOcc(boolean[] t){
          boolean tempOcc = false;
          for(int i=0; i<t.length; i++){
               if(t[i] == true)
                    tempOcc = true;
          }
          return tempOcc;
     }
     public void getDir(){
          boolean changedMain = false;
          boolean changedMsplit = false;
          boolean changedSide = false;

          //checkZero();

          if(mainLine[0] && mainZero){
               mainDir = true;
               changedMain = true;
          }
          if(mainLine[mainLine.length-1] && mainZero){
               if(changedMain)
                    panicMain();
               mainDir = false;
          }
          if(side[side.length-1] && sideZero){
               sideDir = false;
               changedSide = true;
          }
          if(side[0] && sideZero){
               if(changedSide)
                    panicSide();
               sideDir = true;
          }
          if(msplit[msplit.length-1] && msplitZero){
               msplitDir = false;
               changedMsplit = true;
          }
          if(msplit[0] && msplitZero){
               if(changedMsplit)
                    panicMsplit();
               msplitDir = true;
          }
		  //if(id==0)
			//System.out.println("mainDir = "+mainDir+"\nMSplitDIR = "+msplitDir+"\nSideDir = "+sideDir);
          checkZero();
     }
     public void checkZero(){
          for(int i=0; i<mainLine.length; i++){
               if(mainLine[i] == true){
                    mainZero = false;
                    break;
               }
               else{
                    mainZero = true;
               }
          }
          for(int i=0; i<side.length; i++){
               if(side[i] == true){
                    sideZero = false;
                    break;
               }
               else{
                    sideZero = true;
               }
          }
          for(int i=0; i<msplit.length; i++){
               if(msplit[i] == true){
                    msplitZero = false;
                    break;
               }
               else{
                    msplitZero = true;
               }
          }
		  //if(id == 0)
			//System.out.println("mainZero = "+mainZero+"\nMSplitZero = "+msplitZero+"\nSideZero = "+sideZero);
     }
     public void checkCross(){
          if(mainCross && ((mainDir && (mainLine[crossInd] || mainLine[crossInd-1])) || (!mainDir &&(mainLine[crossInd] || mainLine[crossInd+1])))){
               crossPos = true;
               crossLights = true;
          }
          else if(sideCross && ((sideDir && (side[crossInd] || side[crossInd-1])) || (!sideDir &&(mainLine[crossInd] || side[crossInd+1])))){
               crossPos = true;
               crossLights = true;
          }
          else if(msplitCross && ((msplitDir && (msplit[crossInd] || msplit[crossInd-1])) || (!msplitDir &&(msplit[crossInd] || msplit[crossInd+1])))){
               crossPos = true;
               crossLights = true;
          }
          else{
               crossPos = false;
               crossLights = false;
          }
     }
     public void checkLights(){
          if(!lights){
               switchLight = false;
          }
          else{
               if(onSwitch){
                    switchLight = false;
               }
               else{
                    switchLight = true;
               }
          }
     }
     public void checkContinue(){
          for(int i=0; i<mainLine.length-1; i++){
               if((i == mainLine.length-1) && mainDir){
                    if(switchPos){
                         if(mainLine[i] && msplit[1] && !msplit[0]){
                              zeroSpeed(i);
							  //System.out.println("sent 1");
                         }
                    }
                    else{
                         if(mainLine[i] && side[1] && ! side[0]){
                              zeroSpeed(i);
							  //System.out.println("sent 2");
                         }
                    }
               }
               else if((i == mainLine.length-2) && mainDir){
                    if(switchPos){
                         if(mainLine[i] && msplit[0] && !mainLine[i+1]){
                              zeroSpeed(i);
							  //System.out.println("sent 3");
                         }
                    }
                    else{
                         if(mainLine[i] && side[0] && !mainLine[i+1]){
                              zeroSpeed(i);
							  //System.out.println("sent 4");
                         }
                    }
               }
               else{
                    if(mainDir){
                         if(mainLine[i] && mainLine[i+2] && !mainLine[i+1]){
                              zeroSpeed(i);
							  //System.out.println("sent 5");
                         }
                    }
                    else if(i>1){
                         if(mainLine[i] && mainLine[i-2] && !mainLine[i-1]){
                              zeroSpeed(i);
							  //System.out.println("sent 6");
                         }
                    }
               }
          }
          for(int i=0; i<msplit.length-1; i++){
               if(msplitDir && (i < msplit.length-3)){
                    if(msplit[i] && msplit[i+2] && !msplit[i+1]){
                         zeroSpeed(mainLine.length + i);
						 //System.out.println("1");
                    }
               }
               else if(!msplitDir){
                    if(!switchPos && msplit[1]){
                         zeroSpeed(mainLine.length + 1);
						 //System.out.println("1");
                    }
                    if(i==0){
                         if(msplit[i] && mainLine[mainLine.length-2] && !mainLine[mainLine.length-1]){
                              zeroSpeed(mainLine.length + i);
							  //System.out.println("2");
                         }
                    }
                    else if(i==1){
                         if(msplit[i] && mainLine[mainLine.length-1] && !msplit[0]){
                              zeroSpeed(mainLine.length + i);
							  //System.out.println("3");
                         }
                    }
                    else{
                         if(msplit[i] && msplit[i-2] && !msplit[i-1]){
                              zeroSpeed(mainLine.length + i);
							  //System.out.println("4");
                         }
                    }
               }
          }
          for(int i=0; i<side.length-1; i++){
               if(sideDir && (i < side.length-3)){
                    if(side[i] && side[i+2] && !side[i+1]){
                         zeroSpeed(mainLine.length + msplit.length + i);
						 //System.out.println("5");
                    }
               }
               else if(!sideDir){
                    if(switchPos && side[1]){
                         zeroSpeed(mainLine.length + msplit.length + 1);
						 //System.out.println("6");
                    }
                    if(i==0){
                         if(side[i] && mainLine[mainLine.length-2] && !mainLine[mainLine.length-1]){
                              zeroSpeed(mainLine.length + msplit.length + i);
							  //System.out.println("7");
                         }
                    }
                    else if(i==1){
                         if(side[i] && mainLine[mainLine.length-1] && !side[0]){
                              zeroSpeed(mainLine.length + msplit.length +i);
							  //System.out.println("this8");
                         }
                    }
                    else{
                         if(side[i] && side[i-2] && !side[i-1]){
                              zeroSpeed(mainLine.length + msplit.length + i);
							  //System.out.println("9this ");
                         }
                    }
               }
          }
     }
     public boolean checkDist(){
          if(priority){
               if(msplit.length>side.length){
                    for(int i=0; i<side.length;i++){
                         if(msplit[i]){
                              return true;
                         }
                         else if(side[i]){
                              return false;
                         }
                    }
               }
               else{
                    for(int i=0; i<msplit.length;i++){
                         if(msplit[i]){
                              return true;
                         }
                         else if(side[i]){
                              return false;
                         }
                    }
               }
          }
          else{
               if(msplit.length>side.length){
                    for(int i=0; i<side.length;i++){
                         if(side[i]){
                              return false;
                         }
                         else if(msplit[i]){
                              return true;
                         }
                    }
               }
               else{
                    for(int i=0; i<msplit.length;i++){
                         if(side[i]){
                              return false;
                         }
                         else if(msplit[i]){
                              return true;
                         }
                    }
               }
          }
          return true;
     }

     public void zeroSpeedSide(){
          int i, loc;
          for(i=0; i<side.length; i++){
               if(side[i])
                    break;
          }
          loc = mainLine.length+msplit.length+i;
          m = new Message((MDest.TcCtl+id), loc, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
          zeroSpeedSent = true;
     }
     public void zeroSpeedMsplit(){
          int i, loc;
          for(i=0; i<msplit.length; i++){
               if(msplit[i])
                    break;
          }
          loc = mainLine.length+i;
          m = new Message((MDest.TcCtl+id), loc, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
          zeroSpeedSent = true;
     }
     public void zeroSpeedMain(){
          int i, loc;
          for(i=mainLine.length-1; i>=0; i--){
               if(mainLine[i])
                    break;
          }
          loc = i;
          m = new Message((MDest.TcCtl+id), loc, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
          zeroSpeedSent = true;
     }
     public void zeroSpeed(int s){
          m = new Message((MDest.TcCtl+id), s, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
          zeroSpeedSent = true;
     }
     public void panicMain(){
          for(int i=0; i<mainLine.length; i++){
               if(mainLine[i]){
                    m = new Message((MDest.TcCtl+id), i, MType.ZEROSPEED);
                    mq.send(m, MDest.TcMd);
               }
          }
     }
     public void panicMsplit(){
          for(int i=0; i<msplit.length; i++){
               if(msplit[i]){
                    m = new Message((MDest.TcCtl+id), i+mainLine.length, MType.ZEROSPEED);
                    mq.send(m, MDest.TcMd);
               }
          }
     }
     public void panicSide(){
          for(int i=0; i<side.length; i++){
               if(side[i]){
                    m = new Message((MDest.TcCtl+id), i+mainLine.length+msplit.length, MType.ZEROSPEED);
                    mq.send(m, MDest.TcMd);
               }
          }
     }
     public void panic(){
          for(int i=0; i<mainLine.length; i++){
               if(mainLine[i]){
                    m = new Message((MDest.TcCtl+id), i, MType.ZEROSPEED);
                    mq.send(m, MDest.TcMd);
               }
          }
          for(int i=0; i<msplit.length; i++){
               if(msplit[i]){
                    m = new Message((MDest.TcCtl+id), i+mainLine.length, MType.ZEROSPEED);
                    mq.send(m, MDest.TcMd);
               }
          }
          for(int i=0; i<side.length; i++){
               if(side[i]){
                    m = new Message((MDest.TcCtl+id), i+mainLine.length+msplit.length, MType.ZEROSPEED);
                    mq.send(m, MDest.TcMd);
               }
          }
     }
     public Boolean getSwitch(){
          return switchPos;
     }
     public boolean getCross(){
          return crossPos;
     }
     public boolean getSwitchLight(){
          return switchLight;
     }
     public boolean getCrossLight(){
          return crossLights;
     }
     public boolean[] getMainLine(){
          return mainLine;
     }
     public boolean[] getMsplit(){
          return msplit;
     }
     public boolean[] getSide(){
          return side;
     }
     public void setSwitch(boolean s){
          if(!mode)
               switchPos = s;
     }
     public void setCross(boolean c){
          if(!mode)
               crossPos = c;
		   setGUI();
     }
     public void setSwitchLight(boolean s){
          if(!mode)
               switchLight = s;
     }
     public void setCrossLight(boolean c){
          if(!mode)
               crossLights = c;
		   setGUI();
     }
     public void setMode(boolean m){
          mode = m;
     }
     public void setGUI(){
          if(crossExists){
               gui.changeSwitch(switchPos);
               gui.changeSwitchLight(switchLight);

               gui.changeCrossing(crossPos);
               gui.changeCrossLight(crossLights);

               //gui.zeroSpeedSent(zeroSpeedSent);
               gui.updateMain(mainLine);

               gui.updateMsplit(msplit);
               gui.updateSide(side);
          }
          else{
               ngui.changeSwitch(switchPos);
               ngui.changeSwitchLight(switchLight);

               ngui.updateMain(mainLine);

               ngui.updateMsplit(msplit);
               ngui.updateSide(side);
          }

     }
}
