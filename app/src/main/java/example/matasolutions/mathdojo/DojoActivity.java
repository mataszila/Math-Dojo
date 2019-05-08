package example.matasolutions.mathdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class DojoActivity extends AppCompatActivity {

    TextView question_number_textview;
    TextView question_textView;

    Button answer_one_button;
    Button answer_two_button;
    Button answer_three_button;
    Button answer_four_button;

    GameStatistics stats;

    Profile profile;
    ArrayList<Long> answers;

    boolean isUserAlive;
    CurrentQuestion newQuestion;

    Double multiplier;

    TextView dojo_add_score;
    TextView dojo_subtract_score;
    TextView dojo_multiply_score;
    TextView dojo_total_score;

    Long min;
    Long max;

    private Menu menu;

    BottomNavigationView bottomNavigationView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        super.onCreateOptionsMenu(menu);

        this.menu = menu;
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dojo);

        SetupBottomNavigation();

        multiplier = new Double(1);

        min = 2L;
        max = 10L;

        question_number_textview = findViewById(R.id.dojo_question_number_textview);
        question_textView = findViewById(R.id.dojo_question_textview);

        answer_one_button = findViewById(R.id.answer_one_button);
        answer_two_button = findViewById(R.id.answer_two_button);
        answer_three_button = findViewById(R.id.answer_three_button);
        answer_four_button = findViewById(R.id.answer_four_button);


        answers = new ArrayList<>();

        startNewGame();



    }


    private void SetupBottomNavigation(){


            bottomNavigationView = findViewById(R.id.dojo_nav_bar);

            bottomNavigationView.getMenu().add(0, Menu.FIRST, Menu.NONE, "ADD: 0").setIcon(R.drawable.ic_plus_svgrepo_com);
            bottomNavigationView.getMenu().add(0, Menu.FIRST+1, Menu.NONE, "SUBTRACT: 0").setIcon(R.drawable.ic_subtract_svgrepo_com);
            bottomNavigationView.getMenu().add(0, Menu.FIRST+2, Menu.NONE, "MULTIPLY: 0").setIcon(R.drawable.ic_multiply_mathematical_sign_svgrepo_com);
            bottomNavigationView.getMenu().add(0, Menu.FIRST+3, Menu.NONE, "TOTAL: 0").setIcon(R.drawable.ic_baseline_star_24px);

            bottomNavigationView.setLabelVisibilityMode(1);

        }






    private void startNewGame(){

        stats = new GameStatistics(true);

        isUserAlive = true;

        startNewQuestion();

    }

    private Long PickRandomAnswer(){

        Random rn = new Random();

        int min = 0;
        int max = answers.size() - 1;

        int index = rn.nextInt(max - min + 1 ) + min ;


        Long ans = answers.get(index);

        answers.remove(index);

        return ans;

    }

    private void startNewQuestion(){
        newQuestion  = new CurrentQuestion(stats.totalCount+1,min,max);
        question_number_textview.setText("Question " + String.valueOf(stats.totalCount+1));
        question_textView.setText(newQuestion.GetQuestionString());

        answers.add(newQuestion.answer_one);
        answers.add(newQuestion.answer_two);
        answers.add(newQuestion.answer_three);
        answers.add(newQuestion.answer_four);


        answer_one_button.setText(String.valueOf(PickRandomAnswer()));
        answer_two_button.setText(String.valueOf(PickRandomAnswer()));
        answer_three_button.setText(String.valueOf(PickRandomAnswer()));
        answer_four_button.setText(String.valueOf(PickRandomAnswer()));


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v == answer_one_button){
                    CalculateAnswer(answer_one_button);
                }

                if(v == answer_two_button){
                    CalculateAnswer(answer_two_button);
                }
                if(v == answer_three_button){
                    CalculateAnswer(answer_three_button);
                }
                if(v == answer_four_button){
                    CalculateAnswer(answer_four_button);
                }



            }
        };

        answer_one_button.setOnClickListener(onClickListener);
        answer_two_button.setOnClickListener(onClickListener);
        answer_three_button.setOnClickListener(onClickListener);
        answer_four_button.setOnClickListener(onClickListener);



    }

    private void CalculateAnswer(Button button){

        Long answer = Long.parseLong((String) button.getText());

        if(answer.equals(newQuestion.correct_answer)){

            if(newQuestion.type == QuestionType.ADD){
                stats.addition_correct_count++;
                bottomNavigationView.getMenu().getItem(0).setTitle("ADD: " + stats.addition_correct_count);
                //stats.correct_addition();
            }
            if(newQuestion.type == QuestionType.SUBTRACT){
                stats.subtraction_correct_count++;
                bottomNavigationView.getMenu().getItem(1).setTitle("SUBTRACT: " + stats.subtraction_correct_count);
                //stats.correct_subtraction();
            }
            if(newQuestion.type == QuestionType.MULTIPLY){
                stats.multiplication_correct_count++;
                bottomNavigationView.getMenu().getItem(2).setTitle("MULTIPLY: " + stats.multiplication_correct_count);
                //stats.correct_multiplication();
            }

            stats.totalCount++;
            bottomNavigationView.getMenu().getItem(3).setTitle("TOTAL: " + stats.totalCount);


            startNewQuestion();
            increaseMinMax();

        }
        else{
            CurrentQuestion readQuestion = newQuestion;
            endTheGame();
        }



    }

    private void increaseMinMax(){

        multiplier = multiplier * 1.02;

        Double newMin = new Double(min * multiplier);
        Double newMax = new Double(max * multiplier);

        min = newMin.longValue();
        max = newMax.longValue();



    }



    private void endTheGame(){

        Intent intent = new Intent(getApplicationContext(),SummaryActivity.class);
        intent.putExtra("gameStats",stats);
        finish();
        startActivity(intent);





    }




}