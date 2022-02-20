package com.example.guitar_music_app.lecture

import androidx.annotation.RawRes
import com.example.guitar_music_app.R

enum class Chord(val notes: Set<Note>) {
    C_DUR(setOf(Note.C, Note.E, Note.C1)), E_DUR(setOf(Note.E, Note.B1)),
    E_MOL(setOf(Note.G_SHARP1, Note.E, Note.B1)), D_DUR(setOf(Note.F_SHARP, Note.A, Note.D)),
    A_DUR(setOf(Note.C_SHARP, Note.A, Note.E)), A_MOL(setOf(Note.C, Note.A, Note.E)),
    G_DUR(setOf(Note.B1, Note.G1, Note.G)),
//    F_DUR(setOf(Note.F,Note.C, Note.G,Note.D_SHARP1,Note.A_SHARP1, Note.F2, Note.A,Note.F1, Note.C1))
}

enum class Note(@RawRes val tone: Int) {
    F(R.raw.f),
    F_SHARP(R.raw.f_sharp),
    G(R.raw.g),
    G_SHARP(R.raw.g_sharp),
    C(R.raw.c),
    C_SHARP(R.raw.c_sharp),
    D(R.raw.d),
    D_SHARP(R.raw.d_sharp),
    G_SHARP1(R.raw.g_sharp_1),
    A(R.raw.a),
    A_SHARP(R.raw.a_sharp),
    B(R.raw.b),
    D_SHARP1(R.raw.d_sharp_1),
    E(R.raw.e),
    F1(R.raw.f1),
    F_SHARP1(R.raw.f_sharp_1),
    A_SHARP1(R.raw.a_sharp_1),
    B1(R.raw.b1),
    C1(R.raw.c1),
    C_SHARP1(R.raw.c_sharp_1),
    F2(R.raw.f2),
    F_SHARP2(R.raw.f_sharp_2),
    G1(R.raw.g1),
    G_SHARP2(R.raw.g_sharp_2)
}

enum class Tone {
}
