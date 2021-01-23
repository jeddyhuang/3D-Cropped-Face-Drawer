/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author rxiao
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        ArrayList<String> people = new ArrayList<String>();
        people.add("84471");
        people.add("123592");
        people.add("204542");
        people.add("304494");
        people.add("305426");
        people.add("307094");
        people.add("307275");
        people.add("307873");
        people.add("308059");
        people.add("308256");
        people.add("309576");
        people.add("310162");
        people.add("312797");
        people.add("314601");
        people.add("317157");
        people.add("319043");
        people.add("319611");
        people.add("320182");
        people.add("320411");
        people.add("321233");
        people.add("321294");
        people.add("323478");
        people.add("323691");
        people.add("324536");
        people.add("324728");
        people.add("325276");
        people.add("325321");
        people.add("454385");
        people.add("546051");
        people.add("588944");
        people.add("599995");
        people.add("609510");
        people.add("762372");
        people.add("767880");
        people.add("812207");
        //people.add("26792 D");
        //people.add("192641 B");
        //people.add("316137 flag");
        //people.add("460042 E");
        
        String username = System.getProperty("user.name");
        String tempdirectory = "C:\\Users\\" + username + "\\Desktop\\ManualLandmark-JW_1\\460042 E";
        String tempobjdir = tempdirectory + "\\460042.obj";
        String tempselptdir = tempdirectory + "\\460042 SEL.obj";
        ObjReader baseobj = new ObjReader(tempobjdir);
        ObjReader baseselpt = new ObjReader(tempselptdir);
        
        for(int index = 0; index < people.size(); index ++){
            if(index == 0){
                double farthesty = 0;
                double farthestz = 0;
                double sumy = 0;
                double sumz = 0;
                for(int i = 0; i < baseselpt.compileVertices().size(); i ++){
                    sumy += baseselpt.compileVertices().get(i).getY();
                    sumz += baseselpt.compileVertices().get(i).getZ();
                }
                sumy /= baseselpt.compileVertices().size();
                sumz /= baseselpt.compileVertices().size();
                for(int i = 0; i < baseselpt.compileVertices().size(); i ++){
                    if(baseselpt.compileVertices().get(i).getY() < farthesty){
                        farthesty = baseselpt.compileVertices().get(i).getY();
                    }
                    if(baseselpt.compileVertices().get(i).getZ() < farthestz){
                        farthestz = baseselpt.compileVertices().get(i).getZ();
                    }
                }
                double limity = sumy - (sumy - farthesty) * 1.25;
                double limitz = sumz - (sumz - farthestz) * 1.005;
                ArrayList<Vertex> cropped = new ArrayList<Vertex>(baseobj.compileVertices().size());
                for(int i = 0; i < baseobj.compileVertices().size(); i ++){
                    cropped.add(baseobj.compileVertices().get(i));
                }
                for(int i = 0; i < cropped.size(); i ++){
                    if(cropped.get(i).getY() < limity){
                        cropped.remove(i);
                        i--;
                    } else if(cropped.get(i).getZ() < limitz){
                        cropped.remove(i);
                        i--;
                    }
                }
                String ICP_dir = tempdirectory + "\\460042 Cropped.obj";
                PrintWriter writer = new PrintWriter(ICP_dir, "UTF-8");
                for(int i = 0; i < cropped.size(); i ++){
                    writer.print("# ");
                    writer.println(cropped.get(i).getindex());
                    writer.print("v ");
                    writer.print(cropped.get(i).getX());
                    writer.print(" ");
                    writer.print(cropped.get(i).getY());
                    writer.print(" ");
                    writer.println(cropped.get(i).getZ());
                }
                writer.close();
            }
            String name = people.get(index);
            String directory = "C:\\Users\\" + username + "\\Desktop\\ManualLandmark-JW_1\\"  + name;
            String objdir = directory + "\\" + name + ".obj";
            ObjReader compareobj = new ObjReader(objdir);
            
            ArrayList<Vertex> croppedset = new ArrayList<Vertex>(baseselpt.compileVertices().size());
            for(int i = 0; i < baseselpt.compileVertices().size(); i ++){
                Vertex pose = baseselpt.compileVertices().get(i);
                Vertex closest = null;
                double dist = Double.POSITIVE_INFINITY;
                for(int j = 0; j < compareobj.compileVertices().size(); j++){
                    if(-1 < pose.getX() - compareobj.compileVertices().get(j).getX() || pose.getX() - compareobj.compileVertices().get(j).getX() < 1){
                        if(-1 < pose.getY() - compareobj.compileVertices().get(j).getY() || pose.getY() - compareobj.compileVertices().get(j).getY() < 1){
                            if(-1 < pose.getZ() - compareobj.compileVertices().get(j).getZ() || pose.getZ() - compareobj.compileVertices().get(j).getZ() < 1){
                                Vertex potential = compareobj.compileVertices().get(j);
                                double distance = Math.sqrt(Math.pow(pose.getX() - potential.getX(),2) + Math.pow(pose.getY() - potential.getY(),2) + Math.pow(pose.getZ() - potential.getZ(),2));
                                if(distance < dist){
                                    closest = potential;
                                    dist = distance;
                                }
                            }
                        }
                    }
                }
                croppedset.add(closest);
            }
            double farthesty = 0;
            double farthestz = 0;
            double sumy = 0;
            double sumz = 0;
            for(int i = 0; i < croppedset.size(); i ++){
                sumy += croppedset.get(i).getY();
                sumz += croppedset.get(i).getZ();
            }
            sumy /= croppedset.size();
            sumz /= croppedset.size();
            for(int i = 0; i < croppedset.size(); i ++){
                if(croppedset.get(i).getY() < farthesty){
                    farthesty = croppedset.get(i).getY();
                }
                if(croppedset.get(i).getZ() < farthestz){
                    farthestz = croppedset.get(i).getZ();
                }
            }
            double limity = sumy - (sumy - farthesty) * 1.25;
            double limitz = sumz - (sumz - farthestz) * 1.005;
            ArrayList<Vertex> cropped = new ArrayList<Vertex>(compareobj.compileVertices().size());
            for(int i = 0; i < compareobj.compileVertices().size(); i ++){
                cropped.add(compareobj.compileVertices().get(i));
            }
            for(int i = 0; i < cropped.size(); i ++){
                if(cropped.get(i).getY() < limity){
                    cropped.remove(i);
                    i--;
                } else if(cropped.get(i).getZ() < limitz){
                    cropped.remove(i);
                    i--;
                }
            }
            String ICP_dir = directory + "\\" + name + " Cropped.obj";
            PrintWriter writer = new PrintWriter(ICP_dir, "UTF-8");
            for(int i = 0; i < cropped.size(); i ++){
                writer.print("# ");
                writer.println(cropped.get(i).getindex());
                writer.print("v ");
                writer.print(cropped.get(i).getX());
                writer.print(" ");
                writer.print(cropped.get(i).getY());
                writer.print(" ");
                writer.println(cropped.get(i).getZ());
            }
            writer.close();
            System.out.println(name + " Complete");
        }
    }
}
