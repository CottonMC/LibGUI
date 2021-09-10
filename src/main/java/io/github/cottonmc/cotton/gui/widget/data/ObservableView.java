package io.github.cottonmc.cotton.gui.widget.data;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface ObservableView<T> extends Supplier<T> {
	/**
	 * Adds a change listener to this property view.
	 *
	 * @param listener the added listener
	 */
	void addListener(ChangeListener<? super T> listener);

	/**
	 * Removes a change listener from this property view if present.
	 *
	 * @param listener the removed listener
	 */
	void removeListener(ChangeListener<? super T> listener);

	/**
	 * A listener for changes in observable views and properties.
	 *
	 * @param <T> the value type listened to
	 */
	@FunctionalInterface
	interface ChangeListener<T> {
		/**
		 * Handles a change in an observable property.
		 *
		 * @param property
		 * @param from
		 * @param to
		 */
		void onPropertyChange(ObservableView<? extends T> property, @Nullable T from, @Nullable T to);
	}
}
