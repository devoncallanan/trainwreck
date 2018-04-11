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
     private Message m;
     boolean[] mainLine, side, msplit, temp;
     Boolean recSwitch = null, switchPos = true;
     boolean mainDir, sideDir, msplitDir, mainZero = true, sideZero = true, msplitZero = true, mode = true;
     boolean mainCross, sideCross, msplitCross, mainOcc, sideOcc, msplitOcc, switchBias = true, crossPos = false;
     boolean crossLights = false, switchLight = true, loop = false, lights = true, priority = true, onSwitch = false;
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
               }
          }
          for(int i=0; i<r.length; i++){
               if(msplit[i] == true){
                    msplitCross = true;
                    crossInd = i;
                    msplit[i] = false;
               }
          }
          for(int i=0; i<s.length; i++){
               if(side[i] == true){
                    sideCross = true;
                    crossInd = i;
                    side[i] = false;
               }
          }
          checkZero();
     }
     public void run(){
          mReceive();
          getDir();
          if(mainLine[mainLine.length-1])
               onSwitch = true;
          else
               onSwitch = false;
          if(!mode){

          }
          else{
               if(!onSwitch)
                    logic();
               checkLights();
               checkCross();
          }
          checkContinue();
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
          loop = plcCode.getLoop();
          priority = plcCode.getPriority();
          lights = plcCode.getLights();
     }
     public void mReceive(){
          messages = mq.receive(MDest.TcCtl+id);
          while(!messages.isEmpty()){
               m = messages.pop();
               if(m.type() == MType.AUTH){
                    authority.add(m.dataD());
               }
               else if(m.type() == MType.SPEED){
                    speeds.add(m.dataD());
                    System.out.println("TkCon: "+m.dataD());
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
               else if(m.type() == MType.SWITCH){
                    recSwitch = m.dataB();
               }
          }
     }
     public void mSend(){
          for(int i=0; i<speeds.size(); i++){
               m = new Message((MDest.TcCtl+id), speeds.get(i), MType.SPEED);
               System.out.println("TcCtl"+id+": "+speeds.get(i));
               mq.send(m, MDest.TcMd);
               speeds.remove(i);
          }
          for(int i=0; i<authority.size(); i++){
               m = new Message((MDest.TcCtl+id), authority.get(i), MType.AUTH);
               mq.send(m, MDest.TcMd);
               authority.remove(i);
          }

          m = new Message((MDest.TcCtl+id), switchPos, MType.SWITCH);
          System.out.println("SWITCH_FROM:"+m.from());
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
                    if(recSwitch != null){
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
                    if(recSwitch != null){
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
                    if(recSwitch != null){
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
                         switchPos = true;
                    }
                    else{
                         zeroSpeedMsplit(); //msplit
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
                         switchPos = true;
                    }
                    else{
                         zeroSpeedMsplit();
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
          checkZero();
     }
     public void checkZero(){
          for(int i=0; i<mainLine.length; i++){
               if(mainLine[i] == true){
                    mainZero = false;
               }
          }
          for(int i=0; i<side.length; i++){
               if(side[i] == true){
                    sideZero = false;
               }
          }
          for(int i=0; i<msplit.length; i++){
               if(msplit[i] == true){
                    msplitZero = false;
               }
          }
     }
     public void checkCross(){
          if(mainCross && mainLine[crossInd]){
               crossPos = true;
               crossLights = true;
          }
          else if(sideCross && side[crossInd]){
               crossPos = true;
               crossLights = true;
          }
          else if(msplitCross && msplit[crossInd]){
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
               if((i == mainLine.length-2)){
                    if(switchPos){
                         if(mainLine[i] && msplit[0]){
                              if(mainDir && msplitDir){
                                   zeroSpeed(i);
                              }
                              if(!mainDir && !msplitDir){
                                   zeroSpeed(i+1);
                              }
                              if((mainDir && !msplitDir) || (!mainDir && msplitDir)){
                                   zeroSpeed(i);
                                   zeroSpeed(i+1);
                              }
                         }
                         if(mainLine[i] && msplit[1]){
                              if(mainDir && msplitDir){
                                   zeroSpeed(i);
                              }
                              if(!mainDir && !msplitDir){
                                   zeroSpeed(i+2);
                              }
                              if((mainDir && !msplitDir) || (!mainDir && msplitDir)){
                                   zeroSpeed(i);
                                   zeroSpeed(i+2);
                              }
                         }
                    }
                    else{
                         if(mainLine[i] && side[0]){
                              if(mainDir && sideDir){
                                   zeroSpeed(i);
                              }
                              if(!mainDir && !sideDir){
                                   zeroSpeed(i+msplit.length+1);
                              }
                              if((mainDir && !sideDir) || (!mainDir && sideDir)){
                                   zeroSpeed(i);
                                   zeroSpeed(i+msplit.length+1);
                              }
                         }
                         if(mainLine[i] && side[1]){
                              if(mainDir && sideDir){
                                   zeroSpeed(i);
                              }
                              if(!mainDir && !sideDir){
                                   zeroSpeed(i+msplit.length+2);
                              }
                              if((mainDir && !sideDir) || (!mainDir && sideDir)){
                                   zeroSpeed(i);
                                   zeroSpeed(i+msplit.length+2);
                              }
                         }
                    }
               }
               else{
                    if(mainLine[i] && mainLine[i+1]){
                         if(mainDir){
                              zeroSpeed(i);
                              //zeroSpeed(); main[i]
                         }
                         else{
                              zeroSpeed(i+1);
                              //zeroSpeed();main[i+1]
                         }
                    }
                   if(mainLine[i] && mainLine[i+2]){
                         if(mainDir){
                              zeroSpeed(i);
                              //zeroSpeed();//main[i]
                         }
                         else{
                              zeroSpeed(i+2);
                              //zeroSpeed();//main[i+2]
                         }
                    }
               }
          }
          for(int i=0; i<msplit.length-2; i++){
               if(msplit[i] && msplit[i+1]){
                    if(msplitDir){
                         zeroSpeed(i+mainLine.length);
                         //zeroSpeed(); main[i]
                    }
                    else{
                         zeroSpeed(i+1+mainLine.length);
                         //zeroSpeed();main[i+1]
                    }
               }
               if(msplit[i] && msplit[i+2]){
                    if(msplitDir){
                         zeroSpeed(i+mainLine.length);
                         //zeroSpeed();//main[i]
                    }
                    else{
                         zeroSpeed(i+2+mainLine.length);
                         //zeroSpeed();//main[i+2]
                    }
               }
          }
          for(int i=0; i<side.length-2; i++){
               if(side[i] && side[i+1]){
                    if(sideDir){
                         zeroSpeed(i+mainLine.length+msplit.length);
                         //zeroSpeed(); main[i]
                    }
                    else{
                         zeroSpeed(i+1+mainLine.length+msplit.length);
                         //zeroSpeed();main[i+1]
                    }
               }
               if(side[i] && side[i+2]){
                    if(sideDir){
                         zeroSpeed(i+mainLine.length+msplit.length);
                         //zeroSpeed();//main[i]
                    }
                    else{
                         zeroSpeed(i+2+mainLine.length+msplit.length);
                         //zeroSpeed();//main[i+2]
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
     }
     public void zeroSpeed(int s){
          m = new Message((MDest.TcCtl+id), s, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
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
}
