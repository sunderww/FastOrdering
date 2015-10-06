//
//  CommandViewController.h
//  FastOrdering
//
//  Created by Sunder on 04/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OrderMenuModel.h"
#import "OrderALaCarteModel.h"
#import "OrderReviewModel.h"
#import "SLExpandableTableView.h"
#import "CommandMenuViewController.h"
#import "LoaderView.h"
#import "SocketHelper.h"
#import "MainViewController.h"
#import "EditDishViewController.h"

@interface CommandViewController : UIViewController <OrderMenuDelegate, OrderReviewDelegate, CommandMenuDelegate, EditDishDelegate, SocketIODelegate, UIGestureRecognizerDelegate> {
	IBOutlet UIButton *     menuButton;
	IBOutlet UIButton *     alacarteButton;
	IBOutlet UIButton *     reviewButton;
	
	IBOutlet UIButton *		addAlacarteButton;
	
	IBOutlet UILabel *		tableNumberLabel;
	IBOutlet UITextField *	tableNumberField;
	IBOutlet UILabel *		commentLabel;
	IBOutlet UITextView *	commentTextView;
	IBOutlet UILabel *		numPALabel;
	IBOutlet UITextField *	numPAField;
	IBOutlet UIButton *     orderButton;

	IBOutlet UIView *		alacarteView;
	IBOutlet LoaderView *   loaderView;
	
	OrderMenuModel *        menuModel;
	OrderALaCarteModel *    carteModel;
	OrderReviewModel *      reviewModel;
	
	IBOutlet SLExpandableTableView *  menuTableView;
	IBOutlet SLExpandableTableView *  carteTableView;
	IBOutlet SLExpandableTableView *  reviewTableView;
	
	IBOutletCollection(UIView) NSArray * clickedViews;
	IBOutletCollection(UIView) NSArray * contentViews;
	
	UIViewController *      presentController;
	NSTimer *               timer;
	BOOL					didOrder;
	BOOL					forceReview;
}


@property (nonatomic, retain) Order *				order;
@property (nonatomic, retain) MainViewController *	mainController;

- (IBAction)buttonClicked:(UIButton *)sender;
- (IBAction)endEditing;

@end
