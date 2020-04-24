package io.github.cottonmc.cotton.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ValidatedSlot extends Slot {
	private final int slotNumber;
	private boolean modifiable = true;
	
	public ValidatedSlot(Inventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		if (inventoryIn==null) throw new IllegalArgumentException("Can't make an itemslot from a null inventory!");
		this.slotNumber = index;
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return modifiable && inventory.isValid(slotNumber, stack);
	}
	
	@Override
	public boolean canTakeItems(PlayerEntity player) {
		return modifiable && inventory.canPlayerUse(player);
	}
	
	@Override
	public ItemStack getStack() {
		if (inventory==null) {
			System.out.println("Prevented null-inventory from WItemSlot with slot #: "+slotNumber);
			return ItemStack.EMPTY;
		}
		
		ItemStack result = super.getStack();
		if (result==null) {
			System.out.println("Prevented null-itemstack crash from: "+inventory.getClass().getCanonicalName());
			return ItemStack.EMPTY;
		}
		
		return result;
	}

	/**
	 * Returns true if the item in this slot can be modified by players.
	 *
	 * @return true if this slot is modifiable
	 * @since 1.8.0
	 */
	public boolean isModifiable() {
		return modifiable;
	}

	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}

	public int getInventoryIndex() {
		return slotNumber;
	}
}