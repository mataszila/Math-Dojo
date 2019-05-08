package example.matasolutions.mathdojo;

public class Level {

    public int levelNumber;
    public LevelType levelType;

    public Double totalXP;

    public Double totalXP_until_next_level;

    public Double xpLeft;

    public Double starting_xpLeft;

    public Double previous_xpLeft;

    public Level(){

        levelNumber = 1;
        totalXP = new Double(0);


        totalXP_until_next_level = new Double(20);

        starting_xpLeft = new Double(20);

        previous_xpLeft = starting_xpLeft;

        xpLeft = starting_xpLeft;

    }

    public void LevelUp(){


        if(totalXP >= totalXP_until_next_level){

            levelNumber++;

            Double newXp =  (previous_xpLeft/2)  + ((previous_xpLeft/2) * 1.1);

            previous_xpLeft = newXp;

            totalXP_until_next_level = totalXP + newXp.intValue();

            xpLeft = totalXP_until_next_level - totalXP;

        }



    }

    public void addXP(double add){
        totalXP += add;
        LevelUp();


    }







}

