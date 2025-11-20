package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    @DisplayName("Construtor: row e column são guardados corretamente")
    void constructorStoresCoordinates() {
        Position p = new Position(3, 7);
        assertEquals(3, p.getRow());
        assertEquals(7, p.getColumn());
        assertFalse(p.isOccupied());
        assertFalse(p.isHit());
    }

    @Test
    @DisplayName("equals: mesmo objecto (this == otherPosition) devolve true")
    void equalsSameObjectReference() {
        Position p = new Position(2, 4);
        assertTrue(p.equals(p), "Um objecto deve ser igual a ele próprio");
    }

    @Test
    @DisplayName("equals: posições com mesmas coordenadas (instância Position diferente) devolvem true")
    void equalsSameCoordinatesDifferentInstances() {
        Position p1 = new Position(2, 4);
        Position p2 = new Position(2, 4);

        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("equals: ramo instanceof IPosition com objeto Position (cobre o cast) - iguais")
    void equalsCoversInstanceOfBranchWithPositionEqual() {
        Position p1 = new Position(4, 7);
        Position p2 = new Position(4, 7);
        assertTrue(p1.equals(p2)); // rows equal && columns equal -> both subexpressions true
    }

    @Test
    @DisplayName("equals: ramo instanceof IPosition com objeto Position (rows equal, columns different)")
    void equalsCoversInstanceOfBranchWithPositionSameRowDifferentColumn() {
        Position p1 = new Position(4, 7);
        Position p2 = new Position(4, 8);
        // first subexpression (rows equal) true, second (columns equal) false -> forces evaluation of both
        assertFalse(p1.equals(p2));
    }

    @Test
    @DisplayName("equals: ramo instanceof IPosition com objeto Position (rows different, columns same)")
    void equalsCoversInstanceOfBranchWithPositionDifferentRowSameColumn() {
        Position p1 = new Position(4, 7);
        Position p2 = new Position(5, 7);
        // first subexpression false -> short-circuits, second not evaluated
        assertFalse(p1.equals(p2));
    }

    @Test
    @DisplayName("equals: implementação anónima de IPosition com mesmas coordenadas devolve true")
    void equalsWithAnonymousIPositionSameCoordinates() {
        Position p = new Position(5, 6);

        IPosition anon = new IPosition() {
            @Override public int getRow() { return 5; }
            @Override public int getColumn() { return 6; }
            @Override public boolean isAdjacentTo(IPosition other) { return false; }
            @Override public void occupy() {}
            @Override public void shoot() {}
            @Override public boolean isOccupied() { return false; }
            @Override public boolean isHit() { return false; }
        };

        assertTrue(p.equals(anon));
    }

    @Test
    @DisplayName("equals: implementação anónima de IPosition com coordenadas diferentes devolve false")
    void equalsWithAnonymousIPositionDifferentCoordinates() {
        Position p = new Position(5, 6);

        IPosition anon = new IPosition() {
            @Override public int getRow() { return 5; }
            @Override public int getColumn() { return 7; } // coluna diferente
            @Override public boolean isAdjacentTo(IPosition other) { return false; }
            @Override public void occupy() {}
            @Override public void shoot() {}
            @Override public boolean isOccupied() { return false; }
            @Override public boolean isHit() { return false; }
        };

        assertFalse(p.equals(anon));
    }

    @Test
    @DisplayName("equals: comparar com null e com outro tipo devolve false")
    void equalsWithNullOrDifferentType() {
        Position p = new Position(1, 1);

        assertNotEquals(p, null);
        assertNotEquals(p, "not a position");
        assertFalse(p.equals(new Object()));
    }

    @Test
    @DisplayName("isAdjacentTo: posições adjacentes em todas as direções")
    void isAdjacentToAllDirections() {
        Position center = new Position(5, 5);

        assertTrue(center.isAdjacentTo(new Position(4, 5))); // cima
        assertTrue(center.isAdjacentTo(new Position(6, 5))); // baixo
        assertTrue(center.isAdjacentTo(new Position(5, 4))); // esquerda
        assertTrue(center.isAdjacentTo(new Position(5, 6))); // direita

        assertTrue(center.isAdjacentTo(new Position(4, 4))); // diagonal NW
        assertTrue(center.isAdjacentTo(new Position(4, 6))); // NE
        assertTrue(center.isAdjacentTo(new Position(6, 4))); // SW
        assertTrue(center.isAdjacentTo(new Position(6, 6))); // SE
    }

    @Test
    @DisplayName("isAdjacentTo: posições demasiado afastadas não são adjacentes")
    void isNotAdjacentWhenTooFar() {
        Position center = new Position(5, 5);

        assertFalse(center.isAdjacentTo(new Position(7, 5)));
        assertFalse(center.isAdjacentTo(new Position(5, 7)));
        assertFalse(center.isAdjacentTo(new Position(9, 9)));
    }

    @Test
    @DisplayName("occupy marca a posição como ocupada")
    void occupyMarksAsOccupied() {
        Position p = new Position(0, 0);
        assertFalse(p.isOccupied());

        p.occupy();
        assertTrue(p.isOccupied());
    }

    @Test
    @DisplayName("shoot marca a posição como atingida")
    void shootMarksAsHit() {
        Position p = new Position(0, 0);
        assertFalse(p.isHit());

        p.shoot();
        assertTrue(p.isHit());
    }

    @Test
    @DisplayName("toString contém coordenadas")
    void toStringContainsCoordinates() {
        Position p = new Position(3, 8);
        String s = p.toString();

        assertTrue(s.contains("3"));
        assertTrue(s.contains("8"));
    }
}
