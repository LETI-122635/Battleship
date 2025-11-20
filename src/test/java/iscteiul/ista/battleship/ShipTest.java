package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    // ------------------------------------------------------------
    // buildShip (método estático)
    // ------------------------------------------------------------

    @Test
    @DisplayName("buildShip cria Barge para 'barca'")
    void buildShipCreatesBarge() {
        Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(0, 0));
        assertNotNull(s);
        assertTrue(s instanceof Barge);
    }

    @Test
    @DisplayName("buildShip cria Caravel para 'caravela'")
    void buildShipCreatesCaravel() {
        Ship s = Ship.buildShip("caravela", Compass.NORTH, new Position(0, 0));
        assertNotNull(s);
        assertTrue(s instanceof Caravel);
    }

    @Test
    @DisplayName("buildShip cria Carrack para 'nau'")
    void buildShipCreatesCarrack() {
        Ship s = Ship.buildShip("nau", Compass.NORTH, new Position(0, 0));
        assertNotNull(s);
        assertTrue(s instanceof Carrack);
    }

    @Test
    @DisplayName("buildShip cria Frigate para 'fragata'")
    void buildShipCreatesFrigate() {
        Ship s = Ship.buildShip("fragata", Compass.NORTH, new Position(0, 0));
        assertNotNull(s);
        assertTrue(s instanceof Frigate);
    }

    @Test
    @DisplayName("buildShip cria Galleon para 'galeao'")
    void buildShipCreatesGalleon() {
        Ship s = Ship.buildShip("galeao", Compass.NORTH, new Position(0, 0));
        assertNotNull(s);
        assertTrue(s instanceof Galleon);
    }

    @Test
    @DisplayName("buildShip devolve null para tipo de navio desconhecido")
    void buildShipReturnsNullForUnknownKind() {
        Ship s = Ship.buildShip("desconhecido", Compass.NORTH, new Position(0, 0));
        assertNull(s);
    }

    // ------------------------------------------------------------
    // Construtor e getters básicos via DummyShip
    // ------------------------------------------------------------

    @Test
    @DisplayName("Construtor de Ship guarda category, bearing e posição")
    void constructorStoresFields() {
        Position origin = new Position(3, 4);
        List<IPosition> posList = new ArrayList<>();
        posList.add(origin);

        DummyShip ship = new DummyShip("Teste", Compass.EAST, origin, posList);

        assertEquals("Teste", ship.getCategory());
        assertEquals(Compass.EAST, ship.getBearing());
        assertSame(origin, ship.getPosition());
        assertEquals(posList, ship.getPositions());
        assertEquals(1, ship.getSize());
    }

    @Test
    @DisplayName("Construtor de Ship lança AssertionError se bearing for null")
    void constructorThrowsWhenBearingNull() {
        Position origin = new Position(0, 0);
        List<IPosition> posList = new ArrayList<>();
        posList.add(origin);

        assertThrows(AssertionError.class,
                () -> new DummyShip("Teste", null, origin, posList));
    }

    @Test
    @DisplayName("Construtor de Ship lança AssertionError se posição for null")
    void constructorThrowsWhenPositionNull() {
        Position origin = null;
        List<IPosition> posList = new ArrayList<>();
        // até podemos pôr uma posição fictícia aqui, o assert dispara antes
        posList.add(new Position(0, 0));

        assertThrows(AssertionError.class,
                () -> new DummyShip("Teste", Compass.NORTH, origin, posList));
    }

    // ------------------------------------------------------------
    // stillFloating
    // ------------------------------------------------------------

    @Test
    @DisplayName("stillFloating é true se existir pelo menos uma posição não atingida")
    void stillFloatingTrueIfAnyNotHit() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(0, 1);

        List<IPosition> posList = new ArrayList<>();
        posList.add(p1);
        posList.add(p2);

        DummyShip ship = new DummyShip("Teste", Compass.NORTH, p1, posList);

        // acertamos apenas numa posição
        p1.shoot();

        assertTrue(ship.stillFloating());
    }

    @Test
    @DisplayName("stillFloating é false se todas as posições forem atingidas")
    void stillFloatingFalseIfAllHit() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(0, 1);

        List<IPosition> posList = new ArrayList<>();
        posList.add(p1);
        posList.add(p2);

        DummyShip ship = new DummyShip("Teste", Compass.NORTH, p1, posList);

        p1.shoot();
        p2.shoot();

        assertFalse(ship.stillFloating());
    }

    // ------------------------------------------------------------
    // top/bottom/left/right most
    // ------------------------------------------------------------

    @Test
    @DisplayName("getTopMostPos devolve a linha mínima")
    void getTopMostPosReturnsMinRow() {
        Position p1 = new Position(5, 3);
        Position p2 = new Position(2, 4);
        Position p3 = new Position(7, 1);

        List<IPosition> posList = List.of(p1, p2, p3);
        DummyShip ship = new DummyShip("Teste", Compass.NORTH, p1, posList);

        assertEquals(2, ship.getTopMostPos());
    }

    @Test
    @DisplayName("getBottomMostPos devolve a linha máxima")
    void getBottomMostPosReturnsMaxRow() {
        Position p1 = new Position(5, 3);
        Position p2 = new Position(2, 4);
        Position p3 = new Position(7, 1);

        List<IPosition> posList = List.of(p1, p2, p3);
        DummyShip ship = new DummyShip("Teste", Compass.NORTH, p1, posList);

        assertEquals(7, ship.getBottomMostPos());
    }

    @Test
    @DisplayName("getLeftMostPos devolve a coluna mínima")
    void getLeftMostPosReturnsMinColumn() {
        Position p1 = new Position(5, 3);
        Position p2 = new Position(2, 4);
        Position p3 = new Position(7, 1);

        List<IPosition> posList = List.of(p1, p2, p3);
        DummyShip ship = new DummyShip("Teste", Compass.NORTH, p1, posList);

        assertEquals(1, ship.getLeftMostPos());
    }

    @Test
    @DisplayName("getRightMostPos devolve a coluna máxima")
    void getRightMostPosReturnsMaxColumn() {
        Position p1 = new Position(5, 3);
        Position p2 = new Position(2, 4);
        Position p3 = new Position(7, 9);

        List<IPosition> posList = List.of(p1, p2, p3);
        DummyShip ship = new DummyShip("Teste", Compass.NORTH, p1, posList);

        assertEquals(9, ship.getRightMostPos());
    }

    // ------------------------------------------------------------
    // occupies
    // ------------------------------------------------------------

    @Test
    @DisplayName("occupies é true quando a posição pertence ao navio")
    void occupiesReturnsTrueForOwnedPosition() {
        Position p1 = new Position(1, 1);
        Position p2 = new Position(1, 2);

        List<IPosition> posList = List.of(p1, p2);
        DummyShip ship = new DummyShip("Teste", Compass.EAST, p1, posList);

        assertTrue(ship.occupies(new Position(1, 1)));
        assertTrue(ship.occupies(new Position(1, 2)));
    }

    @Test
    @DisplayName("occupies é false quando a posição não pertence ao navio")
    void occupiesReturnsFalseForOtherPosition() {
        Position p1 = new Position(1, 1);
        Position p2 = new Position(1, 2);

        List<IPosition> posList = List.of(p1, p2);
        DummyShip ship = new DummyShip("Teste", Compass.EAST, p1, posList);

        assertFalse(ship.occupies(new Position(0, 0)));
    }

    @Test
    @DisplayName("occupies lança AssertionError se posição for null")
    void occupiesThrowsWhenPosNull() {
        Position p1 = new Position(1, 1);
        List<IPosition> posList = List.of(p1);
        DummyShip ship = new DummyShip("Teste", Compass.EAST, p1, posList);

        assertThrows(AssertionError.class, () -> ship.occupies(null));
    }

    // ------------------------------------------------------------
    // tooCloseTo (IShip e IPosition)
    // ------------------------------------------------------------

    @Test
    @DisplayName("tooCloseTo(IPosition) é true quando alguma posição é adjacente")
    void tooCloseToPositionTrueWhenAdjacent() {
        Position p1 = new Position(5, 5);
        List<IPosition> posList = List.of(p1);
        DummyShip ship = new DummyShip("Teste", Compass.NORTH, p1, posList);

        // posição diagonalmente adjacente
        IPosition near = new Position(6, 6);

        assertTrue(ship.tooCloseTo(near));
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) é false quando nenhuma posição é adjacente")
    void tooCloseToPositionFalseWhenNotAdjacent() {
        Position p1 = new Position(5, 5);
        List<IPosition> posList = List.of(p1);
        DummyShip ship = new DummyShip("Teste", Compass.NORTH, p1, posList);

        IPosition far = new Position(10, 10);

        assertFalse(ship.tooCloseTo(far));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) é true se algum par de posições for adjacente")
    void tooCloseToShipTrueWhenAnyAdjacent() {
        Position p1 = new Position(1, 1);
        DummyShip ship1 = new DummyShip("S1", Compass.NORTH, p1, List.of(p1));

        Position p2 = new Position(2, 2); // adjacente a (1,1)
        DummyShip ship2 = new DummyShip("S2", Compass.SOUTH, p2, List.of(p2));

        assertTrue(ship1.tooCloseTo(ship2));
        assertTrue(ship2.tooCloseTo(ship1));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) é false se nenhum par de posições for adjacente")
    void tooCloseToShipFalseWhenNoAdjacent() {
        Position p1 = new Position(0, 0);
        DummyShip ship1 = new DummyShip("S1", Compass.NORTH, p1, List.of(p1));

        Position p2 = new Position(5, 5);
        DummyShip ship2 = new DummyShip("S2", Compass.SOUTH, p2, List.of(p2));

        assertFalse(ship1.tooCloseTo(ship2));
        assertFalse(ship2.tooCloseTo(ship1));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) lança AssertionError se other for null")
    void tooCloseToShipThrowsWhenOtherNull() {
        Position p1 = new Position(0, 0);
        DummyShip ship = new DummyShip("S", Compass.NORTH, p1, List.of(p1));

        assertThrows(AssertionError.class, () -> ship.tooCloseTo((IShip) null));
    }

    // ------------------------------------------------------------
    // shoot
    // ------------------------------------------------------------

    @Test
    @DisplayName("shoot marca a posição atingida como isHit == true")
    void shootMarksPositionAsHit() {
        Position p1 = new Position(2, 2);
        Position p2 = new Position(2, 3);
        List<IPosition> posList = new ArrayList<>();
        posList.add(p1);
        posList.add(p2);

        DummyShip ship = new DummyShip("Teste", Compass.EAST, p1, posList);

        ship.shoot(new Position(2, 3));

        assertFalse(p1.isHit());
        assertTrue(p2.isHit());
    }

    // ------------------------------------------------------------
    // toString
    // ------------------------------------------------------------

    @Test
    @DisplayName("toString segue o formato [categoria bearing pos]")
    void toStringHasExpectedFormat() {
        Position origin = new Position(1, 2);
        DummyShip ship = new DummyShip("Teste", Compass.WEST, origin, List.of(origin));

        // Ship.toString() faz: "[" + category + " " + bearing + " " + pos + "]"
        String expected = "[" + "Teste" + " " + Compass.WEST + " " + origin + "]";

        assertEquals(expected, ship.toString());
    }


    // ------------------------------------------------------------
    // DummyShip – subclasse concreta para testar Ship
    // ------------------------------------------------------------

    private static class DummyShip extends Ship {

        private final int size;

        DummyShip(String category, Compass bearing, IPosition origin, List<IPosition> positions) {
            super(category, bearing, origin);
            // 'positions' é protected em Ship — podemos atribuir directamente
            this.positions = positions;
            this.size = positions.size();
        }

        @Override
        public Integer getSize() {
            return size;
        }
    }

    @Test
    @DisplayName("shoot não lança exceção quando o navio não tem posições (loop 0 vezes)")
    void shootOnEmptyShipDoesNotThrow() {
        Position origin = new Position(0, 0);
        List<IPosition> emptyPositions = new ArrayList<>();

        DummyShip ship = new DummyShip("Vazio", Compass.NORTH, origin, emptyPositions);

        // pos != null, mas getPositions() está vazio → o for não entra
        assertDoesNotThrow(() -> ship.shoot(new Position(0, 0)));
    }

    @Test
    @DisplayName("getRightMostPos percorre o ciclo com iteração em que não atualiza o right")
    void getRightMostPosHasIterationWithNoUpdate() {
        Position p1 = new Position(0, 5); // primeira coluna = 5
        Position p2 = new Position(0, 3); // menor que 5 → if é falso

        List<IPosition> posList = List.of(p1, p2);
        DummyShip ship = new DummyShip("Teste", Compass.EAST, p1, posList);

        int right = ship.getRightMostPos();

        assertEquals(5, right);
    }

    @Test
    @DisplayName("shoot lança AssertionError se posição for null")
    void shootThrowsWhenPosNull() {
        Position p1 = new Position(0, 0);
        DummyShip ship = new DummyShip("S", Compass.NORTH, p1, List.of(p1));

        assertThrows(AssertionError.class, () -> ship.shoot(null));
    }


}
