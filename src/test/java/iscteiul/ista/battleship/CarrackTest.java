package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarrackTest {

    @Test
    @DisplayName("Carrack NORTH cria 3 posições verticais a partir da posição inicial")
    void constructorNorthCreatesVerticalPositions() {
        IPosition start = new Position(2, 3);

        Carrack carrack = new Carrack(Compass.NORTH, start);

        List<IPosition> positions = carrack.getPositions();
        assertEquals(3, positions.size(), "Nau deve ocupar 3 casas");

        for (int i = 0; i < 3; i++) {
            IPosition p = positions.get(i);
            assertEquals(2 + i, p.getRow(), "linha deve aumentar (NORTH)");
            assertEquals(3, p.getColumn(), "coluna constante (NORTH)");
        }

        assertEquals("Nau", carrack.getCategory());
        assertEquals(Compass.NORTH, carrack.getBearing());
        assertEquals(Integer.valueOf(3), carrack.getSize());
    }

    @Test
    @DisplayName("Carrack SOUTH cria 3 posições verticais a partir da posição inicial")
    void constructorSouthCreatesVerticalPositions() {
        IPosition start = new Position(5, 1);

        Carrack carrack = new Carrack(Compass.SOUTH, start);

        List<IPosition> positions = carrack.getPositions();
        assertEquals(3, positions.size());

        for (int i = 0; i < 3; i++) {
            IPosition p = positions.get(i);
            assertEquals(5 + i, p.getRow(), "linha deve aumentar (SOUTH)");
            assertEquals(1, p.getColumn(), "coluna constante (SOUTH)");
        }

        assertEquals(Compass.SOUTH, carrack.getBearing());
    }

    @Test
    @DisplayName("Carrack EAST cria 3 posições horizontais a partir da posição inicial")
    void constructorEastCreatesHorizontalPositions() {
        IPosition start = new Position(4, 2);

        Carrack carrack = new Carrack(Compass.EAST, start);

        List<IPosition> positions = carrack.getPositions();
        assertEquals(3, positions.size());

        for (int i = 0; i < 3; i++) {
            IPosition p = positions.get(i);
            assertEquals(4, p.getRow(), "linha constante (EAST)");
            assertEquals(2 + i, p.getColumn(), "coluna deve aumentar (EAST)");
        }

        assertEquals(Compass.EAST, carrack.getBearing());
    }

    @Test
    @DisplayName("Carrack WEST cria 3 posições horizontais a partir da posição inicial")
    void constructorWestCreatesHorizontalPositions() {
        IPosition start = new Position(7, 6);

        Carrack carrack = new Carrack(Compass.WEST, start);

        List<IPosition> positions = carrack.getPositions();
        assertEquals(3, positions.size());

        for (int i = 0; i < 3; i++) {
            IPosition p = positions.get(i);
            assertEquals(7, p.getRow(), "linha constante (WEST)");
            // implementação actual: WEST também soma +c
            assertEquals(6 + i, p.getColumn(), "coluna deve aumentar (WEST)");
        }

        assertEquals(Compass.WEST, carrack.getBearing());
    }

    @Test
    @DisplayName("getSize devolve sempre 3 e coincide com o número de posições")
    void sizeIsAlwaysThree() {
        Carrack carrack = new Carrack(Compass.NORTH, new Position(0, 0));

        assertEquals(Integer.valueOf(3), carrack.getSize());
        assertEquals(3, carrack.getPositions().size());
    }
}
