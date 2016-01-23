package com.model;

public enum Rating {
	TERRIBLE (1){
		public String toString(){
			return "Terrible";
		}
	}, 
	POOR (2){
		public String toString(){
			return "Poor";
		}
	},
	AVERAGE (3){
		public String toString(){
			return "Average";
		}
	},
	VERYGOOD (4){
		public String toString(){
			return "Very Good";
		}
	}, 
	EXCELLENT (5){
		public String toString(){
			return "Excellent";
		}
	};
	
	private final int _value;
	
	
	Rating(int value){
		_value = value;
	}
	public int getValue() {
		return _value;
	}
	public boolean equals(Rating rating){
		return rating.getValue()==this.getValue();
	}
	
	// ordinal()|compareTo()|values()|toString()|valueOf()|equals()

}
