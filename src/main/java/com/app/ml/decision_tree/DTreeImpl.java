package com.app.ml.decision_tree;

import java.util.LinkedList;
import java.util.List;

public class DTreeImpl {
	//Penalty factor, threshold ranking value(TRV) TRV = Gain(T,K) - log2(N - 2)/|K|
	//N - distinctive values
	// |K| - number of samples
	// Gain(T,K) = Info(K) - Sum[1-L](|Ki|/|K|)*Info(Ki)
	//Info(K) = - sum[1-C] p(K,j)* log2(p(K,j))
	//j - class, C - class set, p(K,j) - portion of classes in node K, which belongs to jth class
	
	
	
	public class TreeNode{
		private Split split = null;
		private List<TreeNode> children = new LinkedList<>();
		
		public Split getSplit() {
			return split;
		}
		
		public void setSplit(Split split) {
			this.split = split;
		}
		
		public List<TreeNode> getChildren() {
			return children;
		}
		
		public void setChildren(List<TreeNode> children) {
			this.children = children;
		}
	}
}
