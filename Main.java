/*
Trouble setting up the javalin package.
Would need maven but couldn't figure out how to set it up without completely
changing the structure.
*/

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args){
        Javalin app = Javalin.create().port(7070).start();
        
    }
}