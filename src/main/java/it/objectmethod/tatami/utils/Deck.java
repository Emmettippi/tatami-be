package it.objectmethod.tatami.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck<E> extends ArrayList<E> {

	private final static Random random = new Random();

	private static final long serialVersionUID = 1L;

	@SafeVarargs
	public static <E> Deck<E> of(E... deck) {
		return new Deck<E>(deck);
	}

	public static <E> Deck<E> of(List<E> deck) {
		return new Deck<E>(deck);
	}

	public Deck() {
		super();
		this.clear();
	}

	@SafeVarargs
	public Deck(E... deck) {
		super();
		this.clear();
		if (deck != null) {
			for (E e : deck) {
				this.add(e);
			}
		}
	}

	public Deck(List<E> deck) {
		super();
		this.clear();
		this.addAll(deck);
	}

	public void shuffle() {
		int size = this.size();
		for (int i = 0; i < size; i++) {
			this.swap(i, random.nextInt(size));
		}
	}

	public void swap(int a, int b) {
		if (a < 0 || a >= this.size() || b < 0 || b >= this.size()) {
			return;
		}
		E e = this.get(a);
		this.set(a, this.get(b));
		this.set(b, e);
	}

	public E draw() {
		if (!this.isEmpty()) {
			return this.remove(0);
		}
		return null;
	}

	public List<E> draw(int cards) {
		List<E> drawn = new Deck<>(new ArrayList<>());
		while (cards > 0) {
			drawn.add(this.draw());
			cards--;
		}
		return drawn;
	}
}
