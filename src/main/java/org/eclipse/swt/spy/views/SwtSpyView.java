/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.spy.views;

import java.io.*;
import java.net.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;

public class SwtSpyView  {
	
	Composite parent;
	Control control;
	StyledText output;
	Runnable trackWidgets;
	long lastWidget;
	
	public SwtSpyView(Composite parent, Control control) {
		this.parent = parent;
		this.control = control;
		output = new StyledText(parent, SWT.NONE);
		trackWidgets = new Runnable() {
			public void run() {
				if (output == null || output.isDisposed()) return;
				Display display = output.getDisplay();
				if (control == null) {
					output.setText("");
					lastWidget = 0;
				} else {
					if (control.handle != lastWidget) {
						lastWidget = control.handle;
						output.setText(control+"@"+control.handle+"\n");
						output.append("\tStyle: "+getStyle(control)+"\n");
						output.append("\tLayout Data: "+control.getLayoutData()+"\n");
						output.append("\tBounds: "+control.getBounds()+"\n\n");
						if (control instanceof Composite) {
							output.append("\nChildren:\n");
							Control[] children = ((Composite)control).getChildren();
							for (int i = 0; i < children.length; i++) {
								output.append("\t"+children[i]+"\n");
							}
						}
						Composite parent = control.getParent();
						if (parent != null) {
							output.append("\nPeers:\n");
							Control[] peers = parent.getChildren();
							for (int i = 0; i < peers.length; i++) {
								if (peers[i].handle == control.handle) {
									output.append("\t*"+peers[i]+"@"+peers[i].handle+": Layout Data: "+peers[i].getLayoutData()+"\n");
								} else {
									output.append("\t"+peers[i]+"@"+peers[i].handle+": Layout Data: "+peers[i].getLayoutData()+"\n");
								}
							}
							output.append("\nParent Tree:\n");
							Composite[] parents = new Composite[0];
							while (parent != null) {
								Composite[] newParents = new Composite[parents.length + 1];
								System.arraycopy(parents, 0, newParents, 0, parents.length);
								newParents[parents.length] = parent;
								parents = newParents;
								parent = parent.getParent();
							}
							for (int i = parents.length - 1; i >= 0; i--) {
								String prefix = "";
								for (int j = 0; j < parents.length - i - 1; j++) {
									prefix += "\t";
								}
								output.append(prefix + parents[i]+"@"+parents[i].handle+"\n");
								output.append(prefix+"\t Style: "+getStyle(parents[i])+"\n");
								output.append(prefix+"\t Layout: "+parents[i].getLayout()+"\n");
								output.append(prefix+"\t LayoutData: "+parents[i].getLayoutData()+"\n");
							}
							Error error = (Error)control.getData("StackTrace");
							if (error != null) {
								output.append("\nCreation Stack Trace:\n");
								ByteArrayOutputStream stream = new ByteArrayOutputStream();
								PrintStream s = new PrintStream(stream);
								error.printStackTrace(s);
								output.append(stream.toString());
							}
						}
					}
				}
				display.timerExec(100, trackWidgets);
			}

		};
		new Thread(trackWidgets).start();
	}
//	public void createPartControl(Composite parent) {
//		output = new StyledText(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
//
//		actionMonitor = new Action("Monitor", Action.AS_CHECK_BOX) {
//			public void run() {
//				if (actionMonitor.isChecked()) {
//					Display display = output.getDisplay();
//					display.timerExec(100, trackWidgets);
//				}
//			}
//		};
//		actionMonitor.setToolTipText("Enable/Disable monitoring of widgets (ALT+SHIFT+.)");
//		URL installUrl = SwtSpyPlugin.getDefault().getDescriptor().getInstallURL();
//		try {
//			URL imageUrl = new URL(installUrl, "icons/spy.gif");
//			actionMonitor.setImageDescriptor(ImageDescriptor.createFromURL(imageUrl));
//		} catch (MalformedURLException e) {
//			actionMonitor.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
//						getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
//		}
//		actionMonitor.setChecked(false);
//
//		// this is bad code - need to hook into platform global accelerator story
//		parent.getDisplay().addFilter(SWT.KeyUp, new Listener() {
//			public void handleEvent(Event e) {
//				// If this accelerator changes, change the tooltip text
//				if (e.keyCode == '.' && e.stateMask == (SWT.ALT | SWT.SHIFT)) {
//					if (actionMonitor.isChecked()) {
//						actionMonitor.setChecked(false);
//					} else {
//						actionMonitor.setChecked(true);
//						actionMonitor.run();
//					}
//					e.doit = false;
//				};
//			}
//		});
//
//		// hook context menu
//		MenuManager menuMgr = new MenuManager("#PopupMenu");
//		menuMgr.setRemoveAllWhenShown(true);
//		menuMgr.addMenuListener(new IMenuListener() {
//			public void menuAboutToShow(IMenuManager manager) {
//				manager.add(actionMonitor);
//				// Other plug-ins can contribute there actions here
//				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
//			}
//		});
//		Menu menu = menuMgr.createContextMenu(output);
//		output.setMenu(menu);
//
//		// contribute to action bars
//		IActionBars bars = getViewSite().getActionBars();
//		// fill local pull down
//		//bars.getMenuManager().add(actionMonitor);
//		// fill local tool bar
//		bars.getToolBarManager().add(actionMonitor);
//
//	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		output.setFocus();
	}
	String getStyle(Widget w) {
	//MODELESS = 0;
	//BAR = 1 << 1;
	//SEPARATOR = 1 << 1;
	//TOGGLE = 1 << 1;
	//MULTI = 1 << 1;
	//INDETERMINATE = 1 << 1;
	//DBCS = 1 << 1;
	//ALPHA = 1 << 2;
	//TOOL = 1 << 2; 
	//SINGLE = 1 << 2;
	//ARROW = 1 << 2;
	//DROP_DOWN = 1 << 2;
	//SHADOW_IN = 1 << 2;
	//POP_UP = 1 << 3;
	//PUSH = 1 << 3;
	//READ_ONLY = 1 << 3;
	//SHADOW_OUT = 1 << 3;
	//NO_TRIM = 1 << 3;
	//NATIVE = 1 << 3;
	//RESIZE = 1 << 4;
	//SHADOW_ETCHED_IN = 1 << 4;
	//RADIO = 1 << 4;
	//PHONETIC = 1 << 4;
	//ROMAN = 1 << 5;
	//CHECK = 1 << 5;
	//SHADOW_NONE = 1 << 5;
	//TITLE = 1 << 5;
	//DATE = 1 << 5;
	//CLOSE = 1 << 6;
	//MENU = CLOSE;
	//CASCADE = 1 << 6;
	//WRAP = 1 << 6;
	//SIMPLE = 1 << 6;
	//SHADOW_ETCHED_OUT = 1 << 6;
	//MIN = 1 << 7;
	//UP = 1 << 7;
	//TOP = UP;
	//TIME = 1 << 7;
	//HORIZONTAL = 1 << 8;
	//H_SCROLL = 1 << 8;
	//V_SCROLL = 1 << 9;
	//VERTICAL = 1 << 9;
	//MAX = 1 << 10;
	//DOWN               = 1 << 10;
	//BOTTOM             = DOWN;
	//CALENDAR = 1 << 10;
	//BORDER = 1 << 11;
	//CLIP_CHILDREN = 1 << 12; 
	//BALLOON = 1 << 12;
	//CLIP_SIBLINGS = 1 << 13;
	//ON_TOP = 1 << 14;
	//LEAD               = 1 << 14;
	//LEFT               = LEAD;
	//PRIMARY_MODAL = 1 << 15;
	//HIDE_SELECTION = 1 << 15;
	//SHORT = 1 << 15;
	//MEDIUM = 1 << 16;
	//FULL_SELECTION = 1 << 16;
	//SMOOTH = 1 << 16;
	//APPLICATION_MODAL = 1 << 16;
	//SYSTEM_MODAL = 1 << 17;
	//TRAIL              = 1 << 17;	
	//RIGHT              = TRAIL;
	//NO_BACKGROUND = 1 << 18;
	//NO_FOCUS = 1 << 19;
	//NO_REDRAW_RESIZE = 1 << 20;
	//NO_MERGE_PAINTS = 1 << 21;
	//NO_RADIO_GROUP = 1 << 22;
	//PASSWORD = 1 << 22;
	//FLAT = 1 << 23;
	//EMBEDDED = 1 << 24;
	//CENTER = 1 << 24;
	//LEFT_TO_RIGHT = 1 << 25;
	//RIGHT_TO_LEFT = 1 << 26;
	//MIRRORED = 1 << 27;
	//VIRTUAL = 1 << 28;
	//LONG = 1 << 28;
	//DOUBLE_BUFFERED = 1 << 29;
	
		int style = w.getStyle();
		String result = "";
		if (style == SWT.DEFAULT) {
			return "DEFAULT - bad!";
		}
		if ((style & 1 << 1) != 0) {
			if (w instanceof CTabFolder || w instanceof StyledText || w instanceof List || w instanceof Text || w instanceof Table || w instanceof Tree) {
				result += "MULTI | ";
			} else if (w instanceof Menu) {
				result += "BAR | ";
			} else if (w instanceof Label || w instanceof MenuItem || w instanceof ToolItem) {
				result += "SEPARATOR | ";
			} else if (w instanceof Button) {
				result += "TOGGLE | ";
			} else if (w instanceof ProgressBar) {
				result += "INDETERMINATE | ";
			} else {
				result += "BAR or SEPARATOR or TOGGLE or MULTI or INDETERMINATE or DBCS | ";	
			}
		}
		if ((style & 1 << 2) != 0) {
			if (w instanceof Menu || w instanceof ToolItem || w instanceof CoolItem || w instanceof Combo) {
				result += "DROP_DOWN | ";
			} else if (w instanceof Button) {
				result += "ARROW | ";
			} else if (w instanceof CTabFolder || w instanceof StyledText || w instanceof List || w instanceof Text || w instanceof Table || w instanceof Tree) {
				result += "SINGLE | ";
			} else if (w instanceof Label || w instanceof Group) {
				result += "SHADOW_IN | ";
			} else if (w instanceof Decorations) {
				result += "TOOL | ";
			} else {
				result += "ALPHA or TOOL or SINGLE or ARROW or DROP_DOWN or SHADOW_IN | ";
			}
		}
		if ((style & 1 << 3) != 0) {
			if (w instanceof Menu) {
				result += "POP_UP | ";
			} else if (w instanceof Button || w instanceof MenuItem || w instanceof ToolItem) {
				result += "PUSH | ";
			} else if (w instanceof Combo || w instanceof Text || w instanceof StyledText) {
				result += "READ_ONLY | ";
			} else if (w instanceof Label || w instanceof Group || w instanceof ToolBar) {
				result += "SHADOW_OUT | ";
			} else if (w instanceof Decorations) {
				result += "NO_TRIM | ";	
			} else {
				result += "POP_UP or PUSH or READ_ONLY or SHADOW_OUT or NO_TRIM or NATIVE | ";
			}
		}
		if ((style & 1 << 4) != 0) {
			if (w instanceof Button || w instanceof MenuItem || w instanceof ToolItem) {
				result += "RADIO | ";
			} else if (w instanceof Group) {
				result += "SHADOW_ETCHED_IN | ";
			} else if (w instanceof Decorations || w instanceof Tracker) {
				result += "RESIZE | ";
			} else {
				result += "RESIZE or SHADOW_ETCHED_IN or RADIO or PHONETIC | ";
			}
		}
		if ((style & 1 << 5) != 0) {
			if (w instanceof Button || w instanceof MenuItem || w instanceof ToolItem || w instanceof Table || w instanceof Tree) {
				result += "CHECK | ";
			} else if (w instanceof Label || w instanceof Group) {
				result += "SHADOW_NONE | ";
			} else if (w instanceof Decorations) {
				result += "TITLE | ";
			} else if (w instanceof DateTime) {
				result += "DATE | ";
			} else {
				result += "ROMAN or CHECK  or SHADOW_NONE or TITLE | ";
			}
		}
		if ((style & 1 << 6) != 0) {
			if (w instanceof MenuItem) {
				result += "CASCADE | ";
			} else if (w instanceof StyledText || w instanceof Label || w instanceof Text || w instanceof ToolBar) {
				result += "WRAP | ";
			} else if (w instanceof Combo) {
				result += "SIMPLE | ";
			} else if (w instanceof Group) {
				result += "SHADOW_ETCHED_OUT | ";
			} else if (w instanceof Decorations || w instanceof CTabFolder || w instanceof CTabItem) {
				result += "CLOSE | ";
			} else {
				result += "CLOSE or MENU or CASCADE or WRAP or SIMPLE or SHADOW_ETCHED_OUT | ";
			}
		}
		if ((style & 1 << 7) != 0) {
			if (w instanceof Decorations) {
				result += "MIN | ";
			} else if (w instanceof Button || w instanceof Tracker) {
				result += "UP | ";
			} else if (w instanceof CTabFolder) {
				result += "TOP | ";
			} else if (w instanceof DateTime) {
				result += "TIME | ";
			} else {
				result += "MIN or UP or TOP | ";
			}
		}
		if ((style & 1 << 8) != 0) {
			result += "HORIZONTAL | ";
		}
		if ((style & 1 << 9) != 0) {
			result += "VERTICAL | ";
		}
		if ((style & 1 << 10) != 0) {
			if (w instanceof Decorations) {
				result += "MAX | ";
			} else if (w instanceof Button || w instanceof Tracker) {
				result += "DOWN | ";
			} else if (w instanceof CTabFolder) {
				result += "BOTTOM | ";
			} else if (w instanceof DateTime) {
				result += "CALENDAR | ";
			} else {
				result += "MAX or DOWN or BOTTOM | ";
			}
		}
		if ((style & 1 << 11) != 0) {
			result += "BORDER | ";
		}
		if ((style & 1 << 12) != 0) {
			if (w instanceof ToolTip) {
				result += "BALLOON | ";
			} else {
				result += "CLIP_CHILDREN | ";
			}
		}
		if ((style & 1 << 13) != 0) {
			result += "CLIP_SIBLINGS | ";
		}
		if ((style & 1 << 14) != 0) {
			result += "ON_TOP or LEAD or LEFT | ";
		}
		if ((style & 1 << 15) != 0) {
			if (w instanceof Shell) {
				result += "PRIMARY_MODAL | ";
			} else if (w instanceof Table || w instanceof Tree) {
				result += "HIDE_SELECTION | ";
			} else if (w instanceof DateTime) {
				result += "SHORT | ";
			} else {
				result += "PRIMARY_MODAL or HIDE_SELECTION | ";
			}
		}
		if ((style & 1 << 16) != 0) {
			if (w instanceof StyledText || w instanceof Table || w instanceof Tree) {
				result += "FULL_SELECTION | ";
			} else if (w instanceof Shell) {
				result += "APPLICATION_MODAL | ";
			} else if (w instanceof ProgressBar) {
				result += "SMOOTH | ";
			} else if (w instanceof DateTime) {
				result += "MEDIUM | ";
			} else {
				result += "FULL_SELECTION or SMOOTH or APPLICATION_MODAL | ";
			}
		}
		if ((style & 1 << 17) != 0) {
			if (w instanceof Shell) {
				result += "SYSTEM_MODAL | ";
			} else if (w instanceof Button || w instanceof Label || w instanceof TableColumn || w instanceof Tracker || w instanceof ToolBar) {
				result += "TRAIL | ";
			} else {
				result += "SYSTEM_MODAL or TRAIL or RIGHT | ";
			}
		}
		if ((style & 1 << 18) != 0) {
			result += "NO_BACKGROUND | ";
		}
		if ((style & 1 << 19) != 0) {
			result += "NO_FOCUS | ";
		}
		if ((style & 1 << 20) != 0) {
			result += "NO_REDRAW_RESIZE | ";
		}
		if ((style & 1 << 21) != 0) {
			result += "NO_MERGE_PAINTS | ";
		}
		if ((style & 1 << 22) != 0) {
			if (w instanceof Text) {
				result += "PASSWORD | ";
			} else if (w instanceof Composite) {
				result += "NO_RADIO_GROUP | ";
			} else {
				result += "NO_RADIO_GROUP or PASSWORD | ";
			}
		}
		if ((style & 1 << 23) != 0) {
			result += "FLAT | ";
		}
		if ((style & 1 << 24) != 0) {
			if (w instanceof Button || w instanceof Label || w instanceof TableColumn) {
				result += "CENTER | ";
			} else {
				result += "EMBEDDED or CENTER | ";
			}
		}
		if ((style & 1 << 25) != 0) {
			result += "LEFT_TO_RIGHT | ";
		}
		if ((style & 1 << 26) != 0) {
			result += "RIGHT_TO_LEFT | ";
		}
		if ((style & 1 << 27) != 0) {
			result += "MIRRORED | ";
		}
		if ((style & 1 << 28) != 0) {
			if (w instanceof DateTime) {
				result += "LONG | ";
			} else {
				result += "VIRTUAL | ";
			}
		}
		if ((style & 1 << 29) != 0) {
			result += "DOUBLE_BUFFERED | ";
		}
		int lastOr = result.lastIndexOf("|");
		if (lastOr == result.length() - 2 ) result = result.substring(0, result.length() - 2);
		return result;
	}
}