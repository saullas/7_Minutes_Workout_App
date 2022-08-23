package com.example.a7minutesworkout

object Constants {

    const val EXERCISE_TIME = 20 // seconds
    const val REST_TIME = 10 // seconds

    fun defaultExerciseList() : ArrayList<Exercise> {
        val exerciseList = ArrayList<Exercise>()
        exerciseList.add(Exercise(1, "Triceps dips", R.drawable.ic_dibs))
        exerciseList.add(Exercise(2, "Deadbug", R.drawable.ic_deadbug))
        exerciseList.add(Exercise(3, "Push ups", R.drawable.ic_pushups))
        exerciseList.add(Exercise(4, "Squats", R.drawable.ic_squat))
        exerciseList.add(Exercise(5, "Superman", R.drawable.ic_superman))
        exerciseList.add(Exercise(6, "Sit ups", R.drawable.ic_situps))
        exerciseList.add(Exercise(7, "Jumps", R.drawable.ic_jump))
        return exerciseList
    }

    fun restTTS() : ArrayList<String> {
        val sentences = ArrayList<String>()
        sentences.add("Good job. Take a rest.")
        sentences.add("Rest now. You're doing great!")
        sentences.add("Take a rest.")
        sentences.add("Take a rest now.")
        return sentences
    }
}