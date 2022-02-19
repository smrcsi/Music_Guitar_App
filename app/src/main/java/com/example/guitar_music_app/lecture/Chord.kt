package com.example.guitar_music_app.lecture

enum class Chord(val notes: Set<Note>) {
    C_DUR(setOf(Note.C, Note.E, Note.C1)), E_DUR(setOf(Note.E, Note.B1)),
    E_MOL(setOf(Note.G_SHARP1,Note.E, Note.B1)),D_DUR(setOf(Note.F_SHARP,Note.A, Note.D)),
    A_DUR(setOf(Note.C_SHARP,Note.A, Note.E)),A_MOL(setOf(Note.C,Note.A, Note.E)),
    G_DUR(setOf(Note.B1,Note.G2, Note.G)),
//    F_DUR(setOf(Note.F,Note.C, Note.G,Note.D_SHARP1,Note.A_SHARP1, Note.F2, Note.A,Note.F1, Note.C1))
}
enum class Note {
    F, F_SHARP, G, G_SHARP,
    C, C_SHARP, D, D_SHARP,
    G_SHARP1, A, A_SHARP, B,
    D_SHARP1, E, F1, F_SHARP1,
    A_SHARP1, B1, C1, C_SHARP1,
    F2, F_SHARP2, G2, G_SHARP2
}

enum class Tone {
}
