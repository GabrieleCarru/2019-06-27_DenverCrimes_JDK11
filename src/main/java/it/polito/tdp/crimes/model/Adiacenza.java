package it.polito.tdp.crimes.model;

public class Adiacenza {

	private String t1;
	private String t2;
	private Integer peso;
	
	/**
	 * @param t1
	 * @param t2
	 * @param peso
	 */
	public Adiacenza(String t1, String t2, Integer peso) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.peso = peso;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public Integer getPeso() {
		return peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t1 == null) ? 0 : t1.hashCode());
		result = prime * result + ((t2 == null) ? 0 : t2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiacenza other = (Adiacenza) obj;
		if (t1 == null) {
			if (other.t1 != null)
				return false;
		} else if (!t1.equals(other.t1))
			return false;
		if (t2 == null) {
			if (other.t2 != null)
				return false;
		} else if (!t2.equals(other.t2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return t1 + " - " + t2 + "  | " + peso;
	}
	
}
