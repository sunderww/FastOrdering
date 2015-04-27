//
//  HistoryViewController.h
//  FastOrdering
//
//  Created by Sunder on 27/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HistoryViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
  IBOutlet UITableView *  orders;
  NSMutableArray *        data;
}

@end
