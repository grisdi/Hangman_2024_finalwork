package models.datastructures;

import java.time.LocalDateTime;

/**
 * See klass on edetabeli jaoks
 *
 * @param gameTime    Mängu lõpu aeg (mis kuupäeval ja kellaajal mäng lõppes)
 * @param playerName  Mängija nimi
 * @param word        Äraarvatav sõna
 * @param missedChars Valesti sisestatud märgid
 * @param timeSeconds Mängu aeg sekundites. Näiteks 69 (s.o. 1 min ja 9 sek)
 */
public record DataScore(LocalDateTime gameTime, String playerName, String word, String missedChars, int timeSeconds) {
    /**
     * Klassi konstruktor
     *
     * @param gameTime    mänguaegu lõpu aeg
     * @param playerName  mängija nimi
     * @param word        äraarvatav sõna
     * @param missedChars puuduvad tähed
     * @param timeSeconds mängu aeg sekundites
     */
    public DataScore {
    }
}
