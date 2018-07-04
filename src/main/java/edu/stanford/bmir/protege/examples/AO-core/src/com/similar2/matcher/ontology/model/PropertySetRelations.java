package com.similar2.matcher.ontology.model;

public enum PropertySetRelations {
	EQUALS {
		@Override
		public PropertySetRelations inv() {
			return EQUALS;
		}
	}, CONTAINED_BY {
		@Override
		public PropertySetRelations inv() {
			return CONTAINS;
		}
	}, CONTAINS {
		@Override
		public PropertySetRelations inv() {
			return CONTAINED_BY;
		}
	}, DIFFERENT_FROM {
		@Override
		public PropertySetRelations inv() {
			return DIFFERENT_FROM;
		}
	};
	
	public abstract PropertySetRelations inv();
}
