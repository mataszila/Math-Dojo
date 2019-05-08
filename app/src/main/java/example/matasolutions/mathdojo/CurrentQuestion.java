package example.matasolutions.mathdojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentQuestion implements Parcelable {


    public int question_number;

    public Long number_one;
    public Long number_two;

    public QuestionType type;

    public Long answer_one;
    public Long answer_two;
    public Long answer_three;
    public Long answer_four;

    public Long selected_answer;

    public Long correct_answer;

    public long min;


    ArrayList<Long> list;



    public Long getMin() {
        return min;
    }

    public void setMin(Double min) {

        int set = min.intValue();


        this.min = set;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {

        Long set = max;
        this.max = set;
    }

    public Long max;













    public CurrentQuestion(int question_number,Long min, Long max){

        this.min = min;
        this.max = max;

        list = new ArrayList<>();
        GenerateNewQuestion();


    }



    private void GenerateNewQuestion(){

        String formatted_question = " ";

        Random rn = new Random();

        number_one =   min + (long) (Math.random() * (max - min));

        number_two =     min + (long) (Math.random() * (max - min));

        type = GenerateQuestionType();

        switch (type){

            case ADD:
                Generate_ADD(min,max);
                break;
            case SUBTRACT:
                Generate_SUBTRACT(min,max);

                break;
            case MULTIPLY:
                Generate_MULTIPLY(min,max);
                break;

        }

    }

    public String GetQuestionString(){
        StringBuilder sb = new StringBuilder();
        sb.append(number_one);
        sb.append(" " + GetTypeString(type) + " ");
        sb.append(number_two);

        return sb.toString();
    }

    public void updateAnswers(Long min,Long max){
        Random rn = new Random();

        answer_one = min + (long) (Math.random() * (max - min));
        answer_two = min + (long) (Math.random() * (max - min));
        answer_three = min + (long) (Math.random() * (max - min));
        answer_four  = correct_answer;

        reshuffleList();


    }

    public void reshuffleList(){

        list.add(answer_one);
        list.add(answer_two);
        list.add(answer_three);
        list.add(answer_four);

        for(int i = 0;i<list.size()-1;i++){

            for( int j = i+1 ;  j < list.size() ; j ++){

                Long first = list.get(i);
                Long second = list.get(j);

                if(first == second){


                    switch (i){
                        case 0:
                            answer_one = min + (long) (Math.random() * (max - min));
                        break;
                        case 1:
                            answer_two = min + (long) (Math.random() * (max - min));
                            break;
                        case 2:
                            answer_three = min + (long) (Math.random() * (max - min));
                            break;
                        case 3:
                            answer_four  = correct_answer;
                            break;
                    }

                    list.clear();

                    reshuffleList();

                }




            }


        }
        list.clear();




    }



    public void Generate_ADD(Long min, Long max){

        correct_answer = number_one + number_two;


        updateAnswers(min*2,max*2);




    }






    public void Generate_SUBTRACT(Long min, Long max){

        correct_answer  = number_one  - number_two;

        if(correct_answer < 0){

            Long holder = number_one;
            number_one = number_two;
            number_two = holder;

            correct_answer = number_one - number_two;
        }

        updateAnswers(min,max);
    }

    public void Generate_MULTIPLY(Long min, Long max){

        number_one = (long) Math.sqrt(number_one);
        number_two = (long) Math.sqrt(number_two);

        correct_answer = number_one * number_two;

        updateAnswers(number_one*number_two/2,number_one*number_two*2);

    }



    private QuestionType GenerateQuestionType(){

        QuestionType generated_type;

        Random rn = new Random();

        int min = 1;
        int max = 4;

        int selected_number = rn.nextInt(max-min+1) + min;

        switch(selected_number){

            case 1:
                generated_type = QuestionType.ADD;
                break;
            case 2:
                generated_type = QuestionType.SUBTRACT;
                break;
            case 3:
                generated_type = QuestionType.MULTIPLY;
                break;

            default:
                generated_type = QuestionType.ADD;
                break;
        }


        return generated_type;

    }

    private String GetTypeString(QuestionType type){

        switch (type){

            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            default:
                return "+";

        }


    }

    protected CurrentQuestion(Parcel in) {
        question_number = in.readInt();
        number_one = in.readByte() == 0x00 ? null : in.readLong();
        number_two = in.readByte() == 0x00 ? null : in.readLong();
        type = (QuestionType) in.readValue(QuestionType.class.getClassLoader());
        answer_one = in.readByte() == 0x00 ? null : in.readLong();
        answer_two = in.readByte() == 0x00 ? null : in.readLong();
        answer_three = in.readByte() == 0x00 ? null : in.readLong();
        answer_four = in.readByte() == 0x00 ? null : in.readLong();
        selected_answer = in.readByte() == 0x00 ? null : in.readLong();
        correct_answer = in.readByte() == 0x00 ? null : in.readLong();
        min = in.readLong();
        if (in.readByte() == 0x01) {
            list = new ArrayList<Long>();
            in.readList(list, Long.class.getClassLoader());
        } else {
            list = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(question_number);
        if (number_one == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(number_one);
        }
        if (number_two == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(number_two);
        }
        dest.writeValue(type);
        if (answer_one == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(answer_one);
        }
        if (answer_two == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(answer_two);
        }
        if (answer_three == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(answer_three);
        }
        if (answer_four == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(answer_four);
        }
        if (selected_answer == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(selected_answer);
        }
        if (correct_answer == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(correct_answer);
        }
        dest.writeLong(min);
        if (list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(list);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CurrentQuestion> CREATOR = new Parcelable.Creator<CurrentQuestion>() {
        @Override
        public CurrentQuestion createFromParcel(Parcel in) {
            return new CurrentQuestion(in);
        }

        @Override
        public CurrentQuestion[] newArray(int size) {
            return new CurrentQuestion[size];
        }
    };





}
