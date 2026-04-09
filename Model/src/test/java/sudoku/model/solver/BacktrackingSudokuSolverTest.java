package sudoku.model.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sudoku.model.exceptions.FillingBoardSudokuException;
import sudoku.model.models.SudokuBoard;
import static org.junit.jupiter.api.Assertions.*;

public class BacktrackingSudokuSolverTest {

    private BacktrackingSudokuSolver solver;
    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        solver = new BacktrackingSudokuSolver();
        board = new SudokuBoard(solver);
    }

    @Test
    void testSolveEmptyBoard() {
        assertDoesNotThrow(() -> solver.solve(board));
        assertTrue(board.isValidSudoku());
        assertBoardHasNoZeros(board);
    }

    @Test
    void testSolvePartialBoard() {
        board.setField(0, 0, 5);
        board.setField(1, 1, 3);
        board.setField(4, 4, 7);

        assertDoesNotThrow(() -> solver.solve(board));

        assertEquals(5, board.getField(0, 0).getValue());
        assertEquals(3, board.getField(1, 1).getValue());
        assertEquals(7, board.getField(4, 4).getValue());

        assertTrue(board.isValidSudoku());
        assertBoardHasNoZeros(board);
    }

    @Test
    void testSolveNearlyFullBoard() {
        int[][] solvedValues = {
                {3, 2, 7, 6, 5, 1, 4, 8, 9},
                {5, 6, 9, 8, 7, 4, 3, 1, 2},
                {1, 4, 8, 2, 3, 9, 6, 7, 5},
                {7, 3, 6, 5, 4, 2, 8, 9, 1},
                {2, 5, 4, 1, 9, 8, 7, 6, 3},
                {9, 8, 1, 3, 6, 7, 2, 5, 4},
                {8, 9, 2, 4, 1, 6, 5, 3, 7},
                {6, 7, 3, 9, 2, 5, 1, 4, 8},
                {4, 1, 5, 7, 8, 3, 9, 2, 6}
        };

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (row == 8 && col == 8) {
                    continue;
                }
                board.setField(col, row, solvedValues[row][col]);
            }
        }

        assertDoesNotThrow(() -> solver.solve(board));
        assertEquals(6, board.getField(8, 8).getValue());
        assertTrue(board.isValidSudoku());
        assertBoardHasNoZeros(board);
    }

    @Test
    void testSolveAlreadySolvedBoard() {
        int[][] solvedValues = {
                {3, 2, 7, 6, 5, 1, 4, 8, 9},
                {5, 6, 9, 8, 7, 4, 3, 1, 2},
                {1, 4, 8, 2, 3, 9, 6, 7, 5},
                {7, 3, 6, 5, 4, 2, 8, 9, 1},
                {2, 5, 4, 1, 9, 8, 7, 6, 3},
                {9, 8, 1, 3, 6, 7, 2, 5, 4},
                {8, 9, 2, 4, 1, 6, 5, 3, 7},
                {6, 7, 3, 9, 2, 5, 1, 4, 8},
                {4, 1, 5, 7, 8, 3, 9, 2, 6}
        };

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                board.setField(col, row, solvedValues[row][col]);
            }
        }

        assertDoesNotThrow(() -> solver.solve(board));
        assertTrue(board.isValidSudoku());
        assertBoardHasNoZeros(board);
        assertEquals(6, board.getField(8, 8).getValue());
    }

    @Test
    void testSolveImpossibleBoard() {
        board.setField(0, 0, 1);
        board.setField(1, 0, 1);

        assertThrows(FillingBoardSudokuException.class, () -> solver.solve(board));
    }

    @Test
    void testSolveBoardWithBoxConflict() {
        board.setField(0, 0, 4);
        board.setField(1, 1, 4);

        assertThrows(FillingBoardSudokuException.class, () -> solver.solve(board));
    }

    @Test
    void testSolveBoardWithColumnConflict() {
        board.setField(0, 0, 7);
        board.setField(0, 1, 7);

        assertThrows(FillingBoardSudokuException.class, () -> solver.solve(board));
    }

    private void assertBoardHasNoZeros(SudokuBoard board) {
        for (int row = 0; row < SudokuBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < SudokuBoard.BOARD_SIZE; col++) {
                assertNotEquals(0, board.getField(col, row).getValue(),
                        String.format("Field at [%d, %d] should not be zero", col, row));
            }
        }
    }
}
