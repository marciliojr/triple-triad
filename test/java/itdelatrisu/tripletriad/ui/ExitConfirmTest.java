package itdelatrisu.tripletriad.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import itdelatrisu.tripletriad.gfx.Input;

public class ExitConfirmTest {
	@Test
	public void opensOnNoAndConfirmingNoStays() {
		ExitConfirm dialog = new ExitConfirm();
		assertFalse(dialog.isOpen());
		dialog.open();
		assertTrue(dialog.isOpen());
		assertEquals(ExitConfirm.NO, dialog.choice());
		assertEquals(ExitConfirm.Result.STAY, dialog.key(Input.KEY_ENTER));
		assertFalse(dialog.isOpen());
	}

	@Test
	public void movingToYesAndConfirmingExits() {
		ExitConfirm dialog = new ExitConfirm();
		dialog.open();
		assertEquals(ExitConfirm.Result.MOVE, dialog.key(Input.KEY_RIGHT));
		assertEquals(ExitConfirm.YES, dialog.choice());
		assertEquals(ExitConfirm.Result.EXIT, dialog.key(Input.KEY_Z));
		assertFalse(dialog.isOpen());
	}

	@Test
	public void escapeAlwaysStaysEvenOnYes() {
		ExitConfirm dialog = new ExitConfirm();
		dialog.open();
		dialog.key(Input.KEY_LEFT);
		assertEquals(ExitConfirm.YES, dialog.choice());
		assertEquals(ExitConfirm.Result.STAY, dialog.key(Input.KEY_ESCAPE));
		assertFalse(dialog.isOpen());
		// reopening resets to No
		dialog.open();
		assertEquals(ExitConfirm.NO, dialog.choice());
	}

	@Test
	public void otherKeysDoNothingAndClosedDialogIgnoresInput() {
		ExitConfirm dialog = new ExitConfirm();
		assertEquals(ExitConfirm.Result.NONE, dialog.key(Input.KEY_ENTER));
		assertEquals(ExitConfirm.Result.NONE, dialog.answer(ExitConfirm.YES));
		dialog.open();
		assertEquals(ExitConfirm.Result.NONE, dialog.key(Input.KEY_T));
		assertTrue(dialog.isOpen());
		assertEquals(ExitConfirm.Result.EXIT, dialog.answer(ExitConfirm.YES));
		dialog.open();
		assertEquals(ExitConfirm.Result.STAY, dialog.answer(ExitConfirm.NO));
	}
}
