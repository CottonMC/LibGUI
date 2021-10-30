package io.github.cottonmc.test;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class TestDescription extends SyncedGuiDescription {
	private static final Identifier TEST_MESSAGE = new Identifier("libgui", "test");
	private static final Identifier UNREGISTERED_ON_SERVER = new Identifier("libgui", "unregistered_on_server");

	public TestDescription(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(type, syncId, playerInventory, getBlockInventory(context, GuiBlockEntity.INVENTORY_SIZE), null);

		WGridPanel root = (WGridPanel)this.getRootPanel();

		WItemSlot slot = WItemSlot.of(blockInventory, 0, 4, 1);
		root.add(slot, 0, 1);

		WButton buttonA = new WButton(new LiteralText("Send Message"));

		buttonA.setOnClick(() -> {
			ScreenNetworking.of(this, NetworkSide.CLIENT).send(TEST_MESSAGE, buf -> {});
			ScreenNetworking.of(this, NetworkSide.CLIENT).send(UNREGISTERED_ON_SERVER, buf -> {});
		});

		root.add(buttonA, 0, 3, 4, 1);

		WButton buttonB = new WButton(new LiteralText("Show Warnings"));
		buttonB.setOnClick(() -> slot.setIcon(new TextureIcon(new Identifier("libgui-test", "saddle.png"))));

		root.add(buttonB, 5, 3, 4, 1);
		root.add(new WButton(new LiteralText("Button C")), 0, 5, 4, 1);
		root.add(new WButton(new LiteralText("Button D")), 5, 5, 4, 1);
		root.add(new WTextField(new LiteralText("Type something...")).setMaxLength(64), 0, 7, 5, 1);

		root.add(new WLabel(new LiteralText("Large slot:")), 0, 9);
		root.add(WItemSlot.outputOf(blockInventory, 0), 4, 9);

		root.add(WItemSlot.of(blockInventory, 7).setIcon(new TextureIcon(new Identifier("libgui-test", "saddle.png"))), 7, 9);

		root.add(createPlayerInventoryPanel(), 0, 11);
		System.out.println(root.toString());

		this.getRootPanel().validate(this);

		ScreenNetworking.of(this, NetworkSide.SERVER).receive(TEST_MESSAGE, buf -> {
			System.out.println("Received on the server!");
		});

		try {
			slot.onHidden();
			slot.onShown();
		} catch (Throwable t) {
			throw new AssertionError("ValidatedSlot.setVisible crashed", t);
		}
	}
}
