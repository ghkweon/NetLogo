// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.editor

import java.awt.{ event, Point },
  event.{ MouseEvent, MouseListener }

import javax.swing.text.{ Position, JTextComponent }

trait EditorAwareAction extends InstallableAction with MouseListener {
  val EditorKey         = "Editor"
  val LocationKey       = "MouseLocation"
  val DocumentOffsetKey = "DocumentOffset"

  def updateEditorInfo(editor: AbstractEditorArea, mouseLocation: Point, documentOffset: Int): Unit = {
    putValue(EditorKey,         editor)
    putValue(LocationKey,       mouseLocation)
    putValue(DocumentOffsetKey, Int.box(documentOffset))
  }

  def install(editorArea: AbstractEditorArea): Unit = {
    putValue(EditorKey, editorArea)
    editorArea.addMouseListener(this)
  }

  def editor: AbstractEditorArea =
    getValue(EditorKey).asInstanceOf[AbstractEditorArea]
  def location: Point =
    getValue(LocationKey).asInstanceOf[Point]
  def documentOffset: Int =
    getValue(DocumentOffsetKey).asInstanceOf[Integer].intValue

  def mouseClicked(me: MouseEvent): Unit = {}
  def mouseEntered(me: MouseEvent): Unit = {}
  def mouseExited(me: MouseEvent): Unit = {}
  def mousePressed(me: MouseEvent): Unit = {
    if (me.isPopupTrigger) {
      updateMouseValues(me)
    }
  }
  def mouseReleased(me: MouseEvent): Unit = {
    if (me.isPopupTrigger) {
      updateMouseValues(me)
    }
  }

  private def updateMouseValues(me: MouseEvent): Unit = {
    val mouseLocation = me.getComponent.getLocationOnScreen
    mouseLocation.translate(me.getX, me.getY)
    val returnBias = new Array[Position.Bias](1)
    val docOffset = me.getSource match {
      case tc: JTextComponent => tc.getUI.viewToModel2D(tc, me.getPoint, returnBias)
      case _                  => editor.getSelectionEnd
    }
    putValue(LocationKey,       mouseLocation)
    putValue(DocumentOffsetKey, Int.box(docOffset))
  }
}
