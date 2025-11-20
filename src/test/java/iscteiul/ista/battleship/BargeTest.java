package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BargeTest {

    @Test
    @DisplayName("Construtor cria uma barca com uma única posição")
    void constructorCreatesSingleCellBarge() {
        IPosition start = new Position(3, 4);
        Barge barge = new Barge(Compass.NORTH, start);

        List<IPosition> positions = barge.getPositions();

        assertEquals(1, positions.size(), "Barca deve ocupar exatamente 1 casa");
        assertEquals(3, positions.get(0).getRow());
        assertEquals(4, positions.get(0).getColumn());

        assertEquals("Barca", barge.getCategory());
        assertEquals(Compass.NORTH, barge.getBearing());
        assertEquals(Integer.valueOf(1), barge.getSize());
    }

    @Test
    @DisplayName("getPosition devolve a posição inicial correta")
    void getPositionReturnsInitialPosition() {
        Position start = new Position(0, 0);
        Barge barge = new Barge(Compass.WEST, start);

        assertSame(start, barge.getPosition());
    }

    @Test
    @DisplayName("stillFloating() é true antes da barca ser atingida")
    void stillFloatingBeforeBeingHit() {
        Position start = new Position(2, 2);
        Barge barge = new Barge(Compass.SOUTH, start);

        assertTrue(barge.stillFloating());
    }

    @Test
    @DisplayName("shoot marca a posição como atingida e stillFloating() passa a false")
    void shootMarksHit() {
        Position start = new Position(5, 6);
        Barge barge = new Barge(Compass.EAST, start);

        assertTrue(barge.stillFloating());

        barge.shoot(start);

        assertFalse(barge.stillFloating(), "Depois do tiro, a barca deve estar afundada");
    }

    @Test
    @DisplayName("shoot não altera o estado se a posição não coincidir")
    void shootOnWrongPositionDoesNothing() {
        Position start = new Position(1, 1);
        Barge barge = new Barge(Compass.NORTH, start);

        barge.shoot(new Position(9, 9)); // posição errada

        assertTrue(barge.stillFloating(), "Barca não deve afundar com tiro fora");
    }

    @Test
    @DisplayName("toString segue o formato do Ship")
    void toStringFormat() {
        Position start = new Position(4, 4);
        Barge barge = new Barge(Compass.WEST, start);

        // Ship.toString() → "[" + category + " " + bearing + " " + pos + "]"
        String expected = "[" + "Barca" + " " + Compass.WEST + " " + start + "]";

        assertEquals(expected, barge.toString());
    }
}
