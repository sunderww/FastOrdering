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

@interface CommandViewController : UIViewController <OrderMenuDelegate, CommandMenuDelegate, SocketIODelegate> {
	IBOutlet UIButton *     menuButton;
	IBOutlet UIButton *     alacarteButton;
	IBOutlet UIButton *     reviewButton;
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
	Order *                 order;
	NSTimer *               timer;
}

- (IBAction)buttonClicked:(UIButton *)sender;

@end
