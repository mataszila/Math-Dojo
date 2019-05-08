package example.matasolutions.mathdojo;



public class Levels {

    public Level playerLevel;

    public Level  skill_add_level;

    public Level skill_subtract_level;

    public Level skill_multiplication_level;



    public Levels(boolean newPlayer){


        if(newPlayer){

            playerLevel = new Level();
            skill_add_level = new Level();
            skill_subtract_level = new Level();
            skill_multiplication_level = new Level();

        }
    }

    public Levels(){

    }



}
