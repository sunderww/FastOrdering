//
//  MenuViewController.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MenuViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
    IBOutlet UITableView *  tableView;
    IBOutlet UIButton *     backButton;
    
    NSArray *   data;
}

@property (nonatomic, retain) NSString *    parentId;

- (IBAction)goBack;

@end
