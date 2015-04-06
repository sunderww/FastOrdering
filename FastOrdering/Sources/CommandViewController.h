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
#import "SLExpandableTableView.h"
#import "CommandMenuViewController.h"

@interface CommandViewController : UIViewController <OrderALaCarteDelegate, OrderMenuDelegate, CommandMenuDelegate> {
  IBOutlet UIButton *     menuButton;
  IBOutlet UIButton *     alacarteButton;
  IBOutlet UIButton *     reviewButton;
  
  OrderMenuModel *        menuModel;
  OrderALaCarteModel *    carteModel;
  
  IBOutlet SLExpandableTableView *  menuTableView;
  IBOutlet SLExpandableTableView *  carteTableView;
  
  IBOutletCollection(UIView) NSArray * clickedViews;
  IBOutletCollection(UIView) NSArray * contentViews;
  
  UIViewController *      presentController;
  Order * order;
}

- (IBAction)buttonClicked:(UIButton *)sender;

@end
