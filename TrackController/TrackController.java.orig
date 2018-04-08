imporpackage modules.trackcontroller;

import java.util.*;
import shared.*;

public class TrackController {
     ArrayList<Integer> speeds = new ArrayList<Integer>();
     ArrayList<Integer> authority = new ArrayList<Integer>();
     int auth crossInd;
     public MessageQueue mq = new MessageQueue();
     private Stack<Message> messages;
     private Message m;
     boolean[] mainLine, side, msplit, temp;
     Boolean recSwitch = null;
     boolean switchPos, mainDir, sideDir, msplitDir, mainZero = true, sideZero = true, msplitZero = true, mainCross, sideCross, msplitCross, mainOcc, sideOcc, msplitOcc, switchBias = true, crossPos = false, crossLights = false, mainLight = true, sideLight = false, loop = false;
     public TrackController(MessageQueue i, boolean[] n, boolean[] r, boolean[] s){
          mq = i;
          n = main;
          r = msplit;
          s = side;
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
          logic();
          checkCross();
          checkLights();
          mSend();
     }
     public void mReceive(){
          messages = mq.receive(MDest.TcCtl);
          while(!messages.isEmpty()){
               m = messages.pop();
               if(m.type() == MType.AUTH){
                    authority.add(m.dataI());
               }
               else if(m.type() == MType.SPEED){
                    speeds.add(m.dataI());
                    System.out.println("TkCon: "+m.dataI());
               }
          }
     }
     public void mSend(){
          for(int i=0; i<speeds.size(); i++){
               m = new Message(MDest.TcCtl, speeds.get(i), MType.SPEED);
               mq.send(m, MDest.TcMd);
          }
          for(int i=0; i<authority.size(); i++){
               m = new Message(MDest.TcCtl, authority.get(i), MType.AUTH);
               mq.send(m, MDest.TcMd);
          }
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
                         swtichPos = recSwitch;
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
                         swtichPos = recSwitch;
                    }
                    else if(loop){
                              switchPos = true;
                    }
                    else
                         switchPos = switchBias;
               }
               else if(!sideDir && mainDir){
                    boolean closer = checkDistMainSide();
                    if(closer){
                         switchPos = true;
                    }
                    else{
                         switchPos = false;
                    }
               }
               else if(!sideDir && !mainDir){
                    switchPos = false;
               }
          }
          else if(!sideOcc && mainOcc && msplitOcc){
               if(msplitDir && mainDir){
                    if(recSwitch != null){
                         swtichPos = recSwitch;
                    }
                    else if(loop){
                              switchPos = true;
                    }
                    else
                         switchPos = switchBias;
               }
               else if(!msplitDir && mainDir){
                    boolean closer = checkDistMainMsplit();
                    if(closer){
                         switchPos = false;
                    }
                    else{
                         switchPos = true;
                    }
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
                    SwitchPos = true;
               }
               else if(msplitDir && !sideDir && !mainDir){
                    SwitchPos = false;
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
          checkContinue();
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
          boolean changedMSplit = false;
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
                    panicMsplit
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
          else if(msplit && msplit[crossInd]){
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
               mainLight = false;
               sideLight = false;
          }
          else{
               if(switchPos){
                    mainLight = true;
                    sideLight = false;
               }
               else{
                    mainLight = false;
                    sideLight = true;
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
                                   zeroSpeed(i+mslit.length+2);
                              }
                              if((mainDir && !sidetDir) || (!mainDir && sideDir)){
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
     }
     public void zeroSpeedSide(){
          int i, loc;
          for(i=0; i<side.length; i++){
               if(side[i])
                    break;
          }
          loc = mainLine.length+msplit.length+i;
          m = new Message(MDest.TcCtl, loc, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
     }
     public void zeroSpeedMsplit(){
          int i, loc;
          for(i=0; i<msplit.length; i++){
               if(msplit[i])
                    break;
          }
          loc = mainLine.length+i;
          m = new Message(MDest.TcCtl, loc, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
     }
     public void zeroSpeedMain(){
          int i, loc;
          for(i=mainLine.length-1; i>=0; i--){
               if(mainLine[i])
                    break;
          }
          loc = i;
          m = new Message(MDest.TcCtl, loc, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
     }
     public void zeroSpeed(int s){
          m = new Message(MDest.TcCtl, s, MType.ZEROSPEED);
          mq.send(m, MDest.TcMd);
     }
}
