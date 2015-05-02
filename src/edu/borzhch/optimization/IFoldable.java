/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.optimization;

import edu.borzhch.ast.NodeAST;

/**
 * Сворачиваемый узел (constant folding)
 * @author Balushkin M.
 */
public interface IFoldable {
    NodeAST fold();
}
