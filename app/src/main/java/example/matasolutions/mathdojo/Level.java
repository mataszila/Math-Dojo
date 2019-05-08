package example.matasolutions.mathdojo;

public class Level {

    public int levelNumber;
    public LevelType levelType;

    public int totalXP;

    public int totalXP_until_next_level;

    public int xpLeft;

    public int previous_xpLeft;

    public Level(){

        levelNumber = 1;
        totalXP = 0;


        xpLeft = 20;

        totalXP_until_next_level = totalXP + xpLeft;


    }

    public void LevelUp(){


        if(totalXP +xpLeft >= totalXP_until_next_level ){

            previous_xpLeft = xpLeft;
            xpLeft = 0;

        }

        if(totalXP > totalXP_until_next_level){

            levelNumber++;

            Double newXp = new Double(previous_xpLeft + (previous_xpLeft * 1.1));

            xpLeft = newXp.intValue();

            totalXP_until_next_level = totalXP + newXp.intValue();

            xpLeft = totalXP_until_next_level - totalXP;

        }



    }

    public void addXP(int add){
        totalXP += add;
        LevelUp();


    }







}

