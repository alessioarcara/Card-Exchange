package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.places.CardsPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Because Widget creation involves DOM operations, views are relatively expensive to create.
 * It is therefore good practice to make them reusable. An easy way to do this is via view factory.
 */
public class HomeViewImpl extends Composite implements HomeView {
    private static final HomeViewImplUIBinder uiBinder = GWT.create(HomeViewImplUIBinder.class);
    @UiField
    HTMLPanel myPanel;
    @UiField
    DivElement divElement;
    private Presenter presenter;

    public HomeViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        /*
         * Programmatical way to add Button to DOM
         */
        Button btn = new Button("Navigate");
        btn.addClickHandler(e -> presenter.goTo(new CardsPlace()));
        /*
         * When you call add(), Widget.onAttach() is called on the widget that is being
         * added to the panel. onAttach does some work to register the widget to receive
         * events. appendChild() simply attaches one DOM element to another and does
         * nothing else.
         */
        // divElement.appendChild(btn.getElement());
        myPanel.add(btn);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    /*
     * UiBinder instances are factories that generate a UI structure and glue it to
     * an owning Java class.
     * UiBinder<U, O> interface declares two parameter types:
     * U is the type of root element declared in the ui.xml file returned by the
     * createAndBindUi call. -> Widget
     * O is the owner type whose @UiFields are to be filled in. -> HomeViewImpl
     *
     * Any object declared in the ui.xml file, including any DOM elements, can be
     * made available to the owning Java class through its field name
     */
    interface HomeViewImplUIBinder extends UiBinder<Widget, HomeViewImpl> {
    }
}
