//
//  HistoryViewController.h
//  FastOrdering
//
//  Created by Sunder on 27/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainViewController.h"

@interface HistoryViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
	IBOutlet UILabel *		noOrdersLabel;
	IBOutlet UITableView *  orders;
	NSMutableArray *        data;
}

@property (nonatomic, retain) MainViewController * mainController;

@end
