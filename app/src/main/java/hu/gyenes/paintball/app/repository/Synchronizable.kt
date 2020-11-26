package hu.gyenes.paintball.app.repository

interface Synchronizable {
    suspend fun sync()
}